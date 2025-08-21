package com.booquest.booquest_api.application.port.in.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepUpdateStatusResponse;
import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;

public interface UpdateMissionStepStatusUseCase {
    MissionStepUpdateStatusResponse updateStatus(Long stepId, Long userId, StepStatus newStatus);
}
