package com.booquest.booquest_api.application.port.in.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobResponse;

public interface GetUserSideJobUseCase {
    UserSideJobResponse getUserSideJob(Long userId, Long userSideJobId);
}
