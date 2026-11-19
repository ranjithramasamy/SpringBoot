package com.abc.sid.notifsrvc.domain.model;

public record ProfileDefinition(
        String profileId,
        String messageType,
        String messageVersion,
        String targetIsoType,
        String mappingS3Key,
        String rulesS3Key,
        String xsdS3Key
) {}
