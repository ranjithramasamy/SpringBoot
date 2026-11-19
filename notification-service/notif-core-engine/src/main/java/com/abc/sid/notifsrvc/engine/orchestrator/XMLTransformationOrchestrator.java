package com.abc.sid.notifsrvc.engine.orchestrator;

import com.abc.sid.notifsrvc.common.constants.AppConstants;
import com.abc.sid.notifsrvc.common.constants.AuditStatus;
import com.abc.sid.notifsrvc.domain.model.AuditRecord;
import com.abc.sid.notifsrvc.domain.pipeline.PipelineStage;
import com.abc.sid.notifsrvc.domain.pipeline.TransformationContext;
import com.abc.sid.notifsrvc.domain.port.AuditRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Chain of Responsibility Orchestrator executing all registered PipelineStages in deterministic order.
 */
@Service
public class XMLTransformationOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(XMLTransformationOrchestrator.class);

    private final List<PipelineStage> stages;
    private final AuditRepositoryPort auditRepository;

    public XMLTransformationOrchestrator(List<PipelineStage> stages, AuditRepositoryPort auditRepository) {
        this.stages = stages.stream()
                .sorted(Comparator.comparingInt(PipelineStage::getOrder))
                .toList();
        this.auditRepository = auditRepository;
    }

    public TransformationContext orchestrate(String rawJsonPayload) {
        TransformationContext context = new TransformationContext(rawJsonPayload);

        for (PipelineStage stage : stages) {
            String stageName = stage.getClass().getSimpleName();
            try {
                boolean shouldContinue = stage.execute(context);

                if (context.isShortCircuited()) {
                    log.info("Pipeline short-circuited at [{}]: {}", stageName, context.getShortCircuitReason());
                    return context;
                }

                if (!shouldContinue || context.hasErrors()) {
                    log.warn("Pipeline halted at [{}] with errors: {}", stageName, context.getErrors());
                    persistFailureAudit(context, stageName);
                    return context;
                }
            } catch (Exception ex) {
                log.error("Unhandled error in stage [{}]", stageName, ex);
                context.addError(stageName + ": " + ex.getMessage());
                persistFailureAudit(context, stageName);
                return context;
            }
        }

        return context;
    }

    private void persistFailureAudit(TransformationContext context, String failedStage) {
        context.setFinalStatus(AuditStatus.FAILED);
        var proto = context.getEventProto();
        if (proto == null || proto.getMessageId().isBlank()) {
            return; // Payload failed before messageId could be parsed
        }
        String rawReason = "[" + failedStage + "] " + String.join("; ", context.getErrors());
        String boundedReason = rawReason.length() > AppConstants.MAX_AUDIT_ERROR_LENGTH
                ? rawReason.substring(0, AppConstants.MAX_AUDIT_ERROR_LENGTH)
                : rawReason;

        auditRepository.save(new AuditRecord(
                UUID.randomUUID().toString(),
                proto.getMessageId(),
                proto.getEventId().isBlank() ? "UNKNOWN" : proto.getEventId(),
                proto.getMessageType().isBlank() ? "UNKNOWN" : proto.getMessageType(),
                context.getProfileDefinition() != null ? context.getProfileDefinition().profileId() : null,
                AuditStatus.FAILED,
                boundedReason,
                Instant.now()
        ));
    }
}
