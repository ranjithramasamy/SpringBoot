package com.abc.sid.notifsrvc.domain.port;

import com.abc.sid.notifsrvc.common.constants.AuditStatus;
import com.abc.sid.notifsrvc.domain.model.AuditRecord;

public interface AuditRepositoryPort {
    boolean existsByMessageIdAndStatus(String messageId, AuditStatus status);
    void save(AuditRecord record);
}
