package com.abc.sid.notifsrvc.common.exception;

public class XmlTransformationException extends NotificationServiceException {

    public XmlTransformationException(String message, Throwable cause) {
        super("ERR_XML_TRANSFORMATION", message, cause);
    }
}
