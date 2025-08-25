package com.booquest.booquest_api.adapter.in.usersidejob.dto;

import lombok.Builder;

@Builder
public record CurrentMissionProgress(
        Long id,
        String title,
        int orderNo,
        int percent,    // 해당 미션 내부 스텝 진행률
        int completedSteps,
        int totalSteps
) {}
