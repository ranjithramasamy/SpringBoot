package com.abc.sid.notifsrvc.app.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "CA_TRANSFORMATION_AUDIT")
public class TransformationAuditEntity {

    @Id
    @Column(name = "AUDIT_ID", length = 36)
    private String auditId;

    @Column(name = "MESSAGE_ID", nullable = false, unique = true, length = 64)
    private String messageId;

    @Column(name = "EVENT_ID", nullable = false, length = 64)
    private String eventId;

    @Column(name = "MESSAGE_TYPE", nullable = false, length = 32)
    private String messageType;

    @Column(name = "PROFILE_ID", length = 36)
    private String profileId;

    @Column(name = "STATUS", nullable = false, length = 24)
    private String status;

    @Column(name = "ERROR_REASON", length = 1000)
    private String errorReason;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt;

    protected TransformationAuditEntity() {}

    public TransformationAuditEntity(
            String auditId,
            String messageId,
            String eventId,
            String messageType,
            String profileId,
            String status,
            String errorReason,
            Instant createdAt) {
        this.auditId = auditId;
        this.messageId = messageId;
        this.eventId = eventId;
        this.messageType = messageType;
        this.profileId = profileId;
        this.status = status;
        this.errorReason = errorReason;
        this.createdAt = createdAt;
    }

    public String getAuditId() { return auditId; }
    public String getMessageId() { return messageId; }
    public String getEventId() { return eventId; }
    public String getMessageType() { return messageType; }
    public String getProfileId() { return profileId; }
    public String getStatus() { return status; }
    public String getErrorReason() { return errorReason; }
    public Instant getCreatedAt() { return createdAt; }
}
