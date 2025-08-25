package com.booquest.booquest_api.application.port.out.usersidejob;

import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;

import java.util.List;
import java.util.Optional;

public interface UserSideJobRepositoryPort {
    List<UserSideJob> findAllByUserId(Long userId);

    Optional<UserSideJob> findById(Long userSideJobId);

    boolean existsByIdAndUserId(Long userSideJobId, Long userId);

    Optional<UserSideJob> findLatestSideJobForStatus(Long userId);
}
