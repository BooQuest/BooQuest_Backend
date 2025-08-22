package com.booquest.booquest_api.application.service.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobListResponse;
import com.booquest.booquest_api.application.port.in.usersidejob.GetUserSideJobListUseCase;
import com.booquest.booquest_api.application.port.out.usersidejob.UserSideJobRepositoryPort;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserSideJobListService implements GetUserSideJobListUseCase {
    private final UserSideJobRepositoryPort userSideJobRepositoryPort;

    @Override
    public List<UserSideJobListResponse> getUserSideJobList(Long userId) {
        List<UserSideJob> userSideJobs =  userSideJobRepositoryPort.findAllByUserId(userId);

        return userSideJobs.stream()
                .map(UserSideJobListResponse::toResponse)
                .toList();
    }
}
