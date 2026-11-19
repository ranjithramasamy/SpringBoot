package com.abc.sid.notifsrvc.common.exception;

public class MessagePublishException extends NotificationServiceException {

    public MessagePublishException(String message, Throwable cause) {
        super("ERR_KAFKA_PUBLISH", message, cause);
    }
}
