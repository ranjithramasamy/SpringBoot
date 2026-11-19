package com.abc.sid.notifsrvc.app.persistence.repository;

import com.abc.sid.notifsrvc.app.persistence.entity.TransformationAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuditJpaRepository extends JpaRepository<TransformationAuditEntity, String> {

    boolean existsByMessageIdAndStatus(String messageId, String status);

    Optional<TransformationAuditEntity> findByMessageId(String messageId);
}
