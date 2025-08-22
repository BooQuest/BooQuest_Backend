package com.booquest.booquest_api.application.service.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobResponse;
import com.booquest.booquest_api.application.port.in.usersidejob.GetUserSideJobUseCase;
import com.booquest.booquest_api.application.port.out.usersidejob.UserSideJobRepositoryPort;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserSideJobService implements GetUserSideJobUseCase {
    private final UserSideJobRepositoryPort userSideJobRepositoryPort;

    @Override
    public UserSideJobResponse getUserSideJob(Long userId, Long userSideJobId) {
        UserSideJob userSideJob = userSideJobRepositoryPort.findById(userSideJobId)
                .filter(e -> e.getUserId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("UserSideJob not found: " + userSideJobId));

        return UserSideJobResponse.toResponse(userSideJob);
    }
}
