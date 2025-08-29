package com.booquest.booquest_api.adapter.in.missionstep.dto;

import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionStepResponse {
    private Long id;
    private int seq;
    private String title;
    private String status;
    private String detail;

    public static MissionStepResponse toResponse(MissionStep missionStep) {
        return MissionStepResponse.builder()
                .id(missionStep.getId())
                .seq(missionStep.getSeq())
                .title(missionStep.getTitle())
                .status(missionStep.getStatus().name())
                .detail(missionStep.getDetail())
                .build();
    }
}