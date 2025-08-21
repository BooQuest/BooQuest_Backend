package com.booquest.booquest_api.application.port.in.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionResponse;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;

import java.util.List;

public interface GetMissionListUseCase {
    List<MissionResponse> getMissionList(Long userId, MissionStatus status);
}