package com.booquest.booquest_api.application.port.out.activity;

public interface MissionStepQueryPort {
    int countCompletedStepsByUserId(Long userId);
}
