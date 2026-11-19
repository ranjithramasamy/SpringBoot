package com.abc.sid.notifsrvc.domain.port;

import com.abc.sid.notifsrvc.domain.model.ProfileDefinition;
import java.util.Optional;

public interface ProfileRepositoryPort {
    Optional<ProfileDefinition> findActiveProfile(String messageType, String messageVersion);
}
