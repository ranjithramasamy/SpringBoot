package com.abc.sid.notifsrvc.app.persistence.adapter;

import com.abc.sid.notifsrvc.app.persistence.repository.ProfileJpaRepository;
import com.abc.sid.notifsrvc.common.constants.AppConstants;
import com.abc.sid.notifsrvc.domain.model.ProfileDefinition;
import com.abc.sid.notifsrvc.domain.port.ProfileRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaProfileRepositoryAdapter implements ProfileRepositoryPort {

    private final ProfileJpaRepository jpaRepository;

    public JpaProfileRepositoryAdapter(ProfileJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<ProfileDefinition> findActiveProfile(String messageType, String messageVersion) {
        return jpaRepository
                .findByMessageTypeAndMessageVersionAndActiveFlag(messageType, messageVersion, AppConstants.ACTIVE_FLAG)
                .map(e -> new ProfileDefinition(
                        e.getProfileId(),
                        e.getMessageType(),
                        e.getMessageVersion(),
                        e.getTargetIsoType(),
                        e.getMappingS3Key(),
                        e.getRulesS3Key(),
                        e.getXsdS3Key()
                ));
    }
}
