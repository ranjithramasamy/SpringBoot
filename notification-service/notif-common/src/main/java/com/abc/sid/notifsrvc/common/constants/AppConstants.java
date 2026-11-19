package com.abc.sid.notifsrvc.common.constants;

public final class AppConstants {

    private AppConstants() {}

    public static final String DEFAULT_MESSAGE_VERSION = "1.0";
    public static final int ACTIVE_FLAG = 1;
    public static final int INACTIVE_FLAG = 0;

    public static final String HEADER_MESSAGE_ID = "X-Message-Id";
    public static final String HEADER_EVENT_ID = "X-Event-Id";
    public static final String HEADER_ISO_TYPE = "X-Iso-Message-Type";
    public static final String HEADER_PROFILE_ID = "X-Profile-Id";

    public static final int DEFAULT_XML_BUFFER_BYTES = 16384;
    public static final int MAX_AUDIT_ERROR_LENGTH = 1000;
}
