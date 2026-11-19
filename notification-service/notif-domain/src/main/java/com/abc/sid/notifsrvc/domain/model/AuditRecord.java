package com.abc.sid.notifsrvc.domain.model;

import com.abc.sid.notifsrvc.common.constants.AuditStatus;
import java.time.Instant;

public record AuditRecord(
        String auditId,
        String messageId,
        String eventId,
        String messageType,
        String profileId,
        AuditStatus status,
        String errorReason,
        Instant createdAt
) {}
