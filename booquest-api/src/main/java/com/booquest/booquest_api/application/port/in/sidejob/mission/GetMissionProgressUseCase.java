package com.booquest.booquest_api.application.port.in.sidejob.mission;

import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionProgressResponseDto;

public interface GetMissionProgressUseCase {
    MissionProgressResponseDto getMissionProgress(Long userId, Long sideJobId);
} 