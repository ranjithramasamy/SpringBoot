package com.abc.sid.notifsrvc.engine.stage;

import com.abc.sid.notifsrvc.common.constants.AuditStatus;
import com.abc.sid.notifsrvc.common.constants.PipelineStageOrder;
import com.abc.sid.notifsrvc.domain.pipeline.PipelineStage;
import com.abc.sid.notifsrvc.domain.pipeline.TransformationContext;
import com.abc.sid.notifsrvc.domain.port.AuditRepositoryPort;
import org.springframework.stereotype.Component;

/**
 * Stage 2: Prevents duplicate processing and re-emission by checking CA_TRANSFORMATION_AUDIT.
 */
@Component
public class IdempotencyStage implements PipelineStage {

    private final AuditRepositoryPort auditRepository;

    public IdempotencyStage(AuditRepositoryPort auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public boolean execute(TransformationContext context) {
        String messageId = context.getEventProto().getMessageId();
        if (auditRepository.existsByMessageIdAndStatus(messageId, AuditStatus.PUBLISHED)) {
            context.markShortCircuited("Message already PUBLISHED for messageId=" + messageId);
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return PipelineStageOrder.IDEMPOTENCY_CHECK;
    }
}
