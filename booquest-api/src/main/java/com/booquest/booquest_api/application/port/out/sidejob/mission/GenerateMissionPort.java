package com.booquest.booquest_api.application.port.out.sidejob.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.mission.GenerateMissionResult;

public interface GenerateMissionPort {
    GenerateMissionResult generateMission(MissionGenerateRequestDto requestDto);
}
