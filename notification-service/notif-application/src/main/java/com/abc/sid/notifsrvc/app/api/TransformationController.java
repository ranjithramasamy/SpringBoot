package com.abc.sid.notifsrvc.app.api;

import com.abc.sid.notifsrvc.common.constants.AuditStatus;
import com.abc.sid.notifsrvc.domain.pipeline.TransformationContext;
import com.abc.sid.notifsrvc.engine.orchestrator.XMLTransformationOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Operational REST endpoint to trigger or test the XMLTransformationOrchestrator synchronously.
 */
@RestController
@RequestMapping("/api/v1/notifications")
public class TransformationController {

    public record TransformationResponse(
            String status,
            String messageId,
            String eventId,
            String isoMessageType,
            String xmlOutput,
            List<String> errors
    ) {}

    private final XMLTransformationOrchestrator orchestrator;

    public TransformationController(XMLTransformationOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/transform")
    public ResponseEntity<TransformationResponse> transform(@RequestBody String rawJsonPayload) {
        TransformationContext ctx = orchestrator.orchestrate(rawJsonPayload);

        String messageId = ctx.getEventProto() != null ? ctx.getEventProto().getMessageId() : null;
        String eventId = ctx.getEventProto() != null ? ctx.getEventProto().getEventId() : null;
        String isoType = ctx.getProfileDefinition() != null ? ctx.getProfileDefinition().targetIsoType() : null;
        String status = ctx.getFinalStatus() != null ? ctx.getFinalStatus().name() : AuditStatus.FAILED.name();

        TransformationResponse response = new TransformationResponse(
                status,
                messageId,
                eventId,
                isoType,
                ctx.getGeneratedXmlAsString(),
                ctx.getErrors()
        );

        if (ctx.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
