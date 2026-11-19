package com.abc.sid.notifsrvc.engine.stage;

import com.abc.sid.notifsrvc.common.constants.PipelineStageOrder;
import com.abc.sid.notifsrvc.domain.pipeline.PipelineStage;
import com.abc.sid.notifsrvc.domain.pipeline.TransformationContext;
import com.abc.sid.notifsrvc.engine.registry.ProfileRegistryService.ResolvedProfileBundle;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;

/**
 * Stage 5: Validates the emitted UTF-8 XML byte stream against the cached thread-safe XSD Schema.
 */
@Component
public class SchemaValidationStage implements PipelineStage {

    @Override
    public boolean execute(TransformationContext context) {
        try {
            ResolvedProfileBundle bundle = context.getCompiledArtifacts(ResolvedProfileBundle.class);
            Validator validator = bundle.xsdSchema().newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(context.getGeneratedXmlBytes())));
            return true;
        } catch (Exception e) {
            context.addError("XSD schema validation failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getOrder() {
        return PipelineStageOrder.SCHEMA_VALIDATION;
    }
}
