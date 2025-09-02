package com.booquest.booquest_api.application.port.in.missionstep;

import com.booquest.booquest_api.domain.missionstep.model.MissionStep;

import java.util.List;

public interface SelectMissionStepUseCase {
    MissionStep selectMissionStep(Long stepId);
    List<MissionStep> selectMissionStepsByMissionId(Long missionId);
}
