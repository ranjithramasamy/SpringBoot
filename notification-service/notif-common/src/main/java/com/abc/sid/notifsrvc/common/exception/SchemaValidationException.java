package com.abc.sid.notifsrvc.common.exception;

public class SchemaValidationException extends NotificationServiceException {

    public SchemaValidationException(String message, Throwable cause) {
        super("ERR_XSD_SCHEMA_VALIDATION", message, cause);
    }
}
