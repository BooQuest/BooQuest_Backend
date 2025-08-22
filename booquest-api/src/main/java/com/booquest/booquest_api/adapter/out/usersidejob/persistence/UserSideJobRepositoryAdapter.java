package com.booquest.booquest_api.adapter.out.usersidejob.persistence;

import com.booquest.booquest_api.application.port.out.usersidejob.UserSideJobRepositoryPort;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSideJobRepositoryAdapter implements UserSideJobRepositoryPort {

    private final UserSideJobRepository userSideJobRepository;

    @Override
    public List<UserSideJob> findAllByUserId(Long userId) {
        return userSideJobRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<UserSideJob> findById(Long userSideJobId) {
        return userSideJobRepository.findById(userSideJobId);
    }

    @Override
    public boolean existsByIdAndUserId(Long userSideJobId, Long userId) {
        return userSideJobRepository.existsByIdAndUserId(userSideJobId, userId);
    }
}
