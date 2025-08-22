package com.booquest.booquest_api.application.service.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobResponse;
import com.booquest.booquest_api.adapter.out.sidejob.persistence.SideJobRepository;
import com.booquest.booquest_api.adapter.out.usersidejob.persistence.UserSideJobRepository;
import com.booquest.booquest_api.application.port.in.usersidejob.CreateUserSideJobUseCase;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import com.booquest.booquest_api.domain.usersidejob.enums.UserSideJobStatus;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateUserSideJobService implements CreateUserSideJobUseCase {
    private final SideJobRepository sideJobRepository;
    private final UserSideJobRepository userSideJobRepository;

    @Transactional
    @Override
    public UserSideJobResponse createUserSideJob(Long userId, Long sideJobId) {
        SideJob sideJob = getSideJobAndCheckUserPermission(userId, sideJobId);

        boolean exists = userSideJobRepository.existsByUserIdAndSideJobId(userId, sideJobId);
        if(exists) {
            UserSideJob userSideJob = userSideJobRepository.findByUserIdAndSideJobId(userId, sideJobId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("UserSideJob not found (userId: %d, sideJobId: %d)", userId, sideJobId)));
            return UserSideJobResponse.toResponse(userSideJob).withExists(true);
        }

        UserSideJob createdUserSideJob = create(userId, sideJobId, sideJob);
        return UserSideJobResponse.toResponse(createdUserSideJob).withExists(false);
    }

    private SideJob getSideJobAndCheckUserPermission(Long userId, Long sideJobId) {
        SideJob sideJob = sideJobRepository.findById(sideJobId)
                .orElseThrow(() -> new EntityNotFoundException("SideJob not found: " + sideJobId));
        if (!sideJob.getUserId().equals(userId)) {
            throw new IllegalArgumentException("This side job does not belong to you.");
        }
        return sideJob;
    }

    private UserSideJob create(Long userId, Long sideJobId, SideJob sideJob) {
        UserSideJob createdUserSideJob = UserSideJob.builder()
                .userId(userId)
                .sideJobId(sideJobId)
                .title(sideJob.getTitle())
                .description(sideJob.getDescription())
                .status(UserSideJobStatus.IN_PROGRESS)
                .startedAt(LocalDateTime.now())
                .build();
        userSideJobRepository.save(createdUserSideJob);

        sideJob.setSelected(true);
        sideJob.setStartedAt(LocalDateTime.now());

        return createdUserSideJob;
    }
}
