package com.abc.sid.notifsrvc.engine.stage;

import com.abc.sid.notifsrvc.common.constants.AppConstants;
import com.abc.sid.notifsrvc.common.constants.PipelineStageOrder;
import com.abc.sid.notifsrvc.domain.pipeline.PipelineStage;
import com.abc.sid.notifsrvc.domain.pipeline.TransformationContext;
import com.abc.sid.notifsrvc.engine.registry.ProfileRegistryService;
import org.springframework.stereotype.Component;

/**
 * Stage 3: Resolves the active YAML mapping AST, Drools KieBase, and XSD Schema
 * based on incoming message_type and message_version.
 */
@Component
public class ProfileResolutionStage implements PipelineStage {

    private final ProfileRegistryService profileRegistryService;

    public ProfileResolutionStage(ProfileRegistryService profileRegistryService) {
        this.profileRegistryService = profileRegistryService;
    }

    @Override
    public boolean execute(TransformationContext context) {
        var event = context.getEventProto();
        String version = event.getMessageVersion().isBlank()
                ? AppConstants.DEFAULT_MESSAGE_VERSION
                : event.getMessageVersion();

        var bundle = profileRegistryService.resolve(event.getMessageType(), version);
        context.setProfileDefinition(bundle.definition());
        context.setCompiledArtifacts(bundle);
        return true;
    }

    @Override
    public int getOrder() {
        return PipelineStageOrder.PROFILE_RESOLUTION;
    }
}
