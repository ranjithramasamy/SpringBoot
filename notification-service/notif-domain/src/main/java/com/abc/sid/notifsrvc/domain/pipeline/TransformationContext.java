package com.abc.sid.notifsrvc.domain.pipeline;

import com.abc.sid.notifsrvc.common.constants.AuditStatus;
import com.abc.sid.notifsrvc.domain.model.ProfileDefinition;
import com.abc.sid.notifsrvc.domain.proto.CorporateActionEvent;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DDD Aggregate Root representing the stateful lifecycle of a single corporate action transformation.
 */
public class TransformationContext {

    private final String rawJsonPayload;
    private CorporateActionEvent eventProto;
    private ProfileDefinition profileDefinition;
    private Object compiledArtifacts; // Holds ResolvedProfileBundle from core-engine without leaking engine types into domain
    private byte[] generatedXmlBytes;

    private AuditStatus finalStatus;
    private boolean shortCircuited;
    private String shortCircuitReason;
    private final List<String> errors = new ArrayList<>();

    public TransformationContext(String rawJsonPayload) {
        this.rawJsonPayload = rawJsonPayload;
    }

    public String getRawJsonPayload() {
        return rawJsonPayload;
    }

    public CorporateActionEvent getEventProto() {
        return eventProto;
    }

    public void setEventProto(CorporateActionEvent eventProto) {
        this.eventProto = eventProto;
    }

    public ProfileDefinition getProfileDefinition() {
        return profileDefinition;
    }

    public void setProfileDefinition(ProfileDefinition profileDefinition) {
        this.profileDefinition = profileDefinition;
    }

    @SuppressWarnings("unchecked")
    public <T> T getCompiledArtifacts(Class<T> type) {
        return (T) compiledArtifacts;
    }

    public void setCompiledArtifacts(Object compiledArtifacts) {
        this.compiledArtifacts = compiledArtifacts;
    }

    public byte[] getGeneratedXmlBytes() {
        return generatedXmlBytes;
    }

    public void setGeneratedXmlBytes(byte[] generatedXmlBytes) {
        this.generatedXmlBytes = generatedXmlBytes;
    }

    public String getGeneratedXmlAsString() {
        return generatedXmlBytes == null ? null : new String(generatedXmlBytes, StandardCharsets.UTF_8);
    }

    public AuditStatus getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(AuditStatus finalStatus) {
        this.finalStatus = finalStatus;
    }

    public void markShortCircuited(String reason) {
        this.shortCircuited = true;
        this.shortCircuitReason = reason;
        this.finalStatus = AuditStatus.DUPLICATE_SKIPPED;
    }

    public boolean isShortCircuited() {
        return shortCircuited;
    }

    public String getShortCircuitReason() {
        return shortCircuitReason;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
