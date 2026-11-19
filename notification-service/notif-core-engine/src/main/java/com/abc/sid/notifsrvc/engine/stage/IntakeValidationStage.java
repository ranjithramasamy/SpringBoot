package com.abc.sid.notifsrvc.engine.stage;

import com.abc.sid.notifsrvc.common.constants.PipelineStageOrder;
import com.abc.sid.notifsrvc.domain.pipeline.PipelineStage;
import com.abc.sid.notifsrvc.domain.pipeline.TransformationContext;
import com.abc.sid.notifsrvc.domain.proto.CorporateActionEvent;
import com.google.protobuf.util.JsonFormat;
import org.springframework.stereotype.Component;

/**
 * Stage 1: Deserialises raw JSON into a strongly-typed Protobuf CorporateActionEvent
 * and enforces mandatory structural fields.
 */
@Component
public class IntakeValidationStage implements PipelineStage {

    @Override
    public boolean execute(TransformationContext context) {
        if (context.getRawJsonPayload() == null || context.getRawJsonPayload().isBlank()) {
            context.addError("Incoming JSON payload is null or blank");
            return false;
        }
        try {
            CorporateActionEvent.Builder builder = CorporateActionEvent.newBuilder();
            JsonFormat.parser().ignoringUnknownFields().merge(context.getRawJsonPayload(), builder);
            CorporateActionEvent event = builder.build();
            context.setEventProto(event);

            if (event.getMessageId().isBlank()) {
                context.addError("Mandatory field missing: message_id");
            }
            if (event.getEventId().isBlank()) {
                context.addError("Mandatory field missing: event_id");
            }
            if (event.getMessageType().isBlank()) {
                context.addError("Mandatory field missing: message_type");
            }
            return !context.hasErrors();
        } catch (Exception e) {
            context.addError("Invalid JSON payload format: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getOrder() {
        return PipelineStageOrder.INTAKE_VALIDATION;
    }
}
