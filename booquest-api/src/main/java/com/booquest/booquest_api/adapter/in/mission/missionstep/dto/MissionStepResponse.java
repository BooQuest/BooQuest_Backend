package com.booquest.booquest_api.adapter.in.mission.missionstep.dto;

import com.booquest.booquest_api.domain.mission.model.MissionStep;
import com.booquest.booquest_api.domain.sidejob.enums.StepStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionStepResponse {
    private Long id;
    private int seq;
    private String title;
    private StepStatus status;
    private String detail;

    public static MissionStepResponse toResponse(MissionStep missionStep) {
        return MissionStepResponse.builder()
                .id(missionStep.getId())
                .seq(missionStep.getSeq())
                .title(missionStep.getTitle())
                .status(missionStep.getStatus())
                .detail(missionStep.getDetail())
                .build();
    }
}