package com.booquest.booquest_api.application.port.in.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionResponseDto;
import java.util.List;

public interface CreateMissionUseCase {
    List<MissionResponseDto> createMission(Long sideJobId, Long userId);
}
