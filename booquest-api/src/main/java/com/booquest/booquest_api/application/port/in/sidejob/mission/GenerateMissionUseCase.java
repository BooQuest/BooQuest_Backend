package com.booquest.booquest_api.application.port.in.sidejob.mission;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.domain.mission.model.Mission;

import java.util.List;

public interface GenerateMissionUseCase {

    List<Mission> generateMission(MissionGenerateRequestDto requestDto);
}
