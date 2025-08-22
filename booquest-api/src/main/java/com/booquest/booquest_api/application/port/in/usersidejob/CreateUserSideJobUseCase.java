package com.booquest.booquest_api.application.port.in.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobResponse;

public interface CreateUserSideJobUseCase {
    UserSideJobResponse createUserSideJob(Long userId, Long sideJobId);
}
