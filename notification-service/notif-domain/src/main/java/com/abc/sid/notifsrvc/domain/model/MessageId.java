package com.abc.sid.notifsrvc.domain.model;

public record MessageId(String value) {
    public MessageId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("MessageId must not be null or blank");
        }
    }
}
