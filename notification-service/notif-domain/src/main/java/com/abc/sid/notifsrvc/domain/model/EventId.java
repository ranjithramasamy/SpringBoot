package com.abc.sid.notifsrvc.domain.model;

public record EventId(String value) {
    public EventId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("EventId must not be null or blank");
        }
    }
}
