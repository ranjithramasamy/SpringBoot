package com.abc.sid.notifsrvc.app.persistence.adapter;

import com.abc.sid.notifsrvc.app.persistence.entity.TransformationAuditEntity;
import com.abc.sid.notifsrvc.app.persistence.repository.AuditJpaRepository;
import com.abc.sid.notifsrvc.common.constants.AuditStatus;
import com.abc.sid.notifsrvc.domain.model.AuditRecord;
import com.abc.sid.notifsrvc.domain.port.AuditRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class JpaAuditRepositoryAdapter implements AuditRepositoryPort {

    private final AuditJpaRepository jpaRepository;

    public JpaAuditRepositoryAdapter(AuditJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsByMessageIdAndStatus(String messageId, AuditStatus status) {
        return jpaRepository.existsByMessageIdAndStatus(messageId, status.name());
    }

    @Override
    public void save(AuditRecord record) {
        jpaRepository.save(new TransformationAuditEntity(
                record.auditId(),
                record.messageId(),
                record.eventId(),
                record.messageType(),
                record.profileId(),
                record.status().name(),
                record.errorReason(),
                record.createdAt()
        ));
    }
}
