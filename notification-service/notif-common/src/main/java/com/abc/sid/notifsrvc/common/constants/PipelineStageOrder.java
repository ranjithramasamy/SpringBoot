package com.abc.sid.notifsrvc.common.constants;

public final class PipelineStageOrder {

    private PipelineStageOrder() {}

    public static final int INTAKE_VALIDATION = 10;
    public static final int IDEMPOTENCY_CHECK = 20;
    public static final int PROFILE_RESOLUTION = 30;
    public static final int XML_TRANSFORMATION = 40;
    public static final int SCHEMA_VALIDATION = 50;
    public static final int AUDIT_AND_PUBLISH = 60;
}
