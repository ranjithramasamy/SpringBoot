package com.abc.sid.notifsrvc.common.exception;

public class ProfileNotFoundException extends NotificationServiceException {

    public ProfileNotFoundException(String messageType, String version) {
        super("ERR_PROFILE_NOT_FOUND",
              "No active transformation profile found for messageType=" + messageType + ", version=" + version);
    }
}
