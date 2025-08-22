package com.booquest.booquest_api.application.port.in.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobSummaryResponse;

public interface GetUserSideJobSummaryUseCase {
    UserSideJobSummaryResponse getUserSideJobSummary(Long userId, Long userSideJobId);
}
