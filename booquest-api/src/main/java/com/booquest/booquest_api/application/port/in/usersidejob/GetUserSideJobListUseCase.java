package com.booquest.booquest_api.application.port.in.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobListResponse;

import java.util.List;

public interface GetUserSideJobListUseCase {
    List<UserSideJobListResponse> getUserSideJobList(Long userId);
}
