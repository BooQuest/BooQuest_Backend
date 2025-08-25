package com.booquest.booquest_api.application.port.out.activity;

public interface UserSideJobQueryPort {
    int countCompletedSideJobsByUserId(Long userId);
}
