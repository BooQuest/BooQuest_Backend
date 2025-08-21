package com.booquest.booquest_api.application.port.out.sidejob.mission.missionstep;

import com.booquest.booquest_api.adapter.in.mission.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.sidejob.mission.missionstep.GenerateMissionStepResult;

public interface GenerateMissionStepPort {
    GenerateMissionStepResult generateMissionStep(MissionStepGenerateRequestDto requestDto);
}
