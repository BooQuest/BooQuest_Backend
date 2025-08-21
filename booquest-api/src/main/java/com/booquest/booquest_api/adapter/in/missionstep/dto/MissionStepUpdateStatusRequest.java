package com.booquest.booquest_api.adapter.in.missionstep.dto;

import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionStepUpdateStatusRequest {
    private StepStatus status;
}
