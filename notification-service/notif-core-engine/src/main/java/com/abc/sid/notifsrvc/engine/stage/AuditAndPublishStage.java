package com.abc.sid.notifsrvc.engine.stage;

import com.abc.sid.notifsrvc.common.constants.AppConstants;
import com.abc.sid.notifsrvc.common.constants.AuditStatus;
import com.abc.sid.notifsrvc.common.constants.PipelineStageOrder;
import com.abc.sid.notifsrvc.domain.model.AuditRecord;
import com.abc.sid.notifsrvc.domain.pipeline.PipelineStage;
import com.abc.sid.notifsrvc.domain.pipeline.TransformationContext;
import com.abc.sid.notifsrvc.domain.port.AuditRepositoryPort;
import com.abc.sid.notifsrvc.domain.port.SwiftPublisherPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Stage 6: Publishes XSD-validated ISO 20022 XML to outbound SWIFT Kafka topic (keyed by eventId)
 * and persists the PUBLISHED audit record.
 */
@Component
public class AuditAndPublishStage implements PipelineStage {

    private final SwiftPublisherPort swiftPublisher;
    private final AuditRepositoryPort auditRepository;

    public AuditAndPublishStage(SwiftPublisherPort swiftPublisher, AuditRepositoryPort auditRepository) {
        this.swiftPublisher = swiftPublisher;
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional
    public boolean execute(TransformationContext context) {
        var event = context.getEventProto();
        var profile = context.getProfileDefinition();

        Map<String, String> headers = Map.of(
                AppConstants.HEADER_MESSAGE_ID, event.getMessageId(),
                AppConstants.HEADER_EVENT_ID, event.getEventId(),
                AppConstants.HEADER_ISO_TYPE, profile.targetIsoType(),
                AppConstants.HEADER_PROFILE_ID, profile.profileId()
        );

        swiftPublisher.publish(event.getEventId(), context.getGeneratedXmlBytes(), headers);

        auditRepository.save(new AuditRecord(
                UUID.randomUUID().toString(),
                event.getMessageId(),
                event.getEventId(),
                event.getMessageType(),
                profile.profileId(),
                AuditStatus.PUBLISHED,
                null,
                Instant.now()
        ));

        context.setFinalStatus(AuditStatus.PUBLISHED);
        return true;
    }

    @Override
    public int getOrder() {
        return PipelineStageOrder.AUDIT_AND_PUBLISH;
    }
}
