package com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto;

import lombok.Builder;

@Builder
public record MissionProgressResponseDto(
    Long currentMissionId,
    Integer currentMissionOrder,
    String currentMissionTitle,
    Double missionStepProgressPercentage
) {
} 