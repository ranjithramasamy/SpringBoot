package com.abc.sid.notifsrvc.common.exception;

public class IntakeValidationException extends NotificationServiceException {

    public IntakeValidationException(String message) {
        super("ERR_INTAKE_VALIDATION", message);
    }

    public IntakeValidationException(String message, Throwable cause) {
        super("ERR_INTAKE_VALIDATION", message, cause);
    }
}
