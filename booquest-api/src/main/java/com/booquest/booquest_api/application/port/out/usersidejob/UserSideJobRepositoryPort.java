package com.booquest.booquest_api.application.port.out.usersidejob;

import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;

import java.util.List;
import java.util.Optional;

public interface UserSideJobRepositoryPort {
    List<UserSideJob> findAllByUserId(Long userId);

    Optional<UserSideJob> findById(Long userSideJobId);

    boolean existsByIdAndUserId(Long userSideJobId, Long userId);

    Optional<UserSideJob> findLatestSideJobForStatus(Long userId);

    long deleteByUserId(Long userId);

    Optional<UserSideJob> findBySideJobId(Long id);

    Optional<UserSideJob> findLatestSideJobInProgress(Long userId);

    Optional<UserSideJob> findLatestSideJobAnyStatus(Long userId); // 최신 1건 (상태 무관)
}
