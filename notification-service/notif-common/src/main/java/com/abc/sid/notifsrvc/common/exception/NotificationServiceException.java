package com.abc.sid.notifsrvc.common.exception;

public class NotificationServiceException extends RuntimeException {

    private final String errorCode;

    public NotificationServiceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public NotificationServiceException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
