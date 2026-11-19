package com.abc.sid.notifsrvc.engine.stage;

import com.abc.sid.notifsrvc.common.constants.PipelineStageOrder;
import com.abc.sid.notifsrvc.domain.pipeline.PipelineStage;
import com.abc.sid.notifsrvc.domain.pipeline.TransformationContext;
import com.abc.sid.notifsrvc.domain.proto.CorporateActionEvent;
import com.abc.sid.notifsrvc.domain.proto.EnrichmentData;
import com.abc.sid.notifsrvc.engine.mapping.ProtobufToMapConverter;
import com.abc.sid.notifsrvc.engine.registry.ProfileRegistryService.ResolvedProfileBundle;
import com.abc.sid.notifsrvc.engine.rules.DroolsRuleEngine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Stage 4: Enriches the canonical Protobuf domain model, executes Drools business rule validation,
 * and streams the final ISO 20022 XML via CompiledYamlXmlMapper + Woodstox StAX.
 */
@Component
public class XMLTransformationStage implements PipelineStage {

    private final DroolsRuleEngine droolsRuleEngine;

    public XMLTransformationStage(DroolsRuleEngine droolsRuleEngine) {
        this.droolsRuleEngine = droolsRuleEngine;
    }

    @Override
    public boolean execute(TransformationContext context) {
        // 1. Domain Enrichment
        CorporateActionEvent enrichedEvent = enrich(context.getEventProto());
        context.setEventProto(enrichedEvent);

        ResolvedProfileBundle bundle = context.getCompiledArtifacts(ResolvedProfileBundle.class);

        // 2. Drools Business Rule Validation
        List<String> ruleViolations = droolsRuleEngine.validate(bundle.droolsKieBase(), enrichedEvent);
        if (!ruleViolations.isEmpty()) {
            ruleViolations.forEach(context::addError);
            return false;
        }

        // 3. Direct Protobuf -> Map -> Woodstox StAX UTF-8 XML Streaming
        Map<String, Object> payloadMap = ProtobufToMapConverter.toMap(enrichedEvent);
        byte[] xmlBytes = bundle.xmlMapper().transformToXmlBytes(payloadMap);
        context.setGeneratedXmlBytes(xmlBytes);

        return true;
    }

    private CorporateActionEvent enrich(CorporateActionEvent event) {
        if (!event.hasEnrichment()) {
            return event.toBuilder()
                    .setEnrichment(EnrichmentData.newBuilder()
                            .setSenderBic("ABCSIDBBXXX")
                            .setReceiverBic("CLIENTBICXX")
                            .setDepositoryCode("DTCC")
                            .build())
                    .build();
        }
        return event;
    }

    @Override
    public int getOrder() {
        return PipelineStageOrder.XML_TRANSFORMATION;
    }
}
