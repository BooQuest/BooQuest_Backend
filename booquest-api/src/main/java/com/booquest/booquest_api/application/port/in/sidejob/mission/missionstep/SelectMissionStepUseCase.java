package com.booquest.booquest_api.application.port.in.sidejob.mission.missionstep;

import com.booquest.booquest_api.domain.mission.model.MissionStep;

import java.util.List;

public interface SelectMissionStepUseCase {
    MissionStep selectMissionStep(Long stepId);
}
