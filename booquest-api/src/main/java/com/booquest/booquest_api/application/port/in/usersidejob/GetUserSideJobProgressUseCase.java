package com.booquest.booquest_api.application.port.in.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobProgressResponse;

public interface GetUserSideJobProgressUseCase {
    UserSideJobProgressResponse getUserSideJobProgress(Long userId);
}
