package com.booquest.booquest_api.application.port.in.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponseDto;
import java.util.List;

public interface CreateMissionStepUseCase {
    List<MissionStepResponseDto> createMissionSteps(Long missionId);
}
