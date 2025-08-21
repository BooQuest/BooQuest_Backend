package com.booquest.booquest_api.application.port.in.sidejob.mission.missionstep;

import com.booquest.booquest_api.adapter.in.mission.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.domain.mission.model.MissionStep;

import java.util.List;

public interface GenerateMissionStepUseCase {
    List<MissionStep> generateMissionStep(MissionStepGenerateRequestDto requestDto);
}
