package com.booquest.booquest_api.application.port.out.sidejob.mission;

import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GenerateMissionResult;

public interface GenerateMissionPort {
    GenerateMissionResult generateMission(MissionGenerateRequestDto requestDto);
}
