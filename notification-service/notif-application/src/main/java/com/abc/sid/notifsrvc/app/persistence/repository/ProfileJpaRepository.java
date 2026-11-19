package com.abc.sid.notifsrvc.app.persistence.repository;

import com.abc.sid.notifsrvc.app.persistence.entity.TransformationProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileJpaRepository extends JpaRepository<TransformationProfileEntity, String> {

    Optional<TransformationProfileEntity> findByMessageTypeAndMessageVersionAndActiveFlag(
            String messageType, String messageVersion, Integer activeFlag);
}
