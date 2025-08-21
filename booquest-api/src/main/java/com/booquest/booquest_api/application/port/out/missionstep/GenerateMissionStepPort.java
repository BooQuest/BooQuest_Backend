package com.booquest.booquest_api.application.port.out.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.missionstep.GenerateMissionStepResult;

public interface GenerateMissionStepPort {
    GenerateMissionStepResult generateMissionStep(MissionStepGenerateRequestDto requestDto);
}
