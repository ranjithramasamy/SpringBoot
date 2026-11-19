package com.abc.sid.notifsrvc.domain.pipeline;

/**
 * Chain of Responsibility handler contract for ordered transformation stages.
 */
public interface PipelineStage {

    /**
     * Executes this stage against the shared TransformationContext.
     *
     * @param context Aggregate root holding state across pipeline stages
     * @return true if execution should advance to the next stage; false to halt or short-circuit
     */
    boolean execute(TransformationContext context);

    /**
     * Determines deterministic execution ordering in the pipeline.
     */
    int getOrder();
}
