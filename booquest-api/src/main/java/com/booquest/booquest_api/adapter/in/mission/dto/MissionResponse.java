package com.booquest.booquest_api.adapter.in.mission.dto;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponse;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.enums.MissionStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MissionResponse {
    private Long id;
    private Long sideJobId;
    private String title;
    private MissionStatus status;
    private int orderNo;
    private String designNotes;
    private List<MissionStepResponse> steps;

    public static MissionResponse toResponse(Mission mission) {
        List<MissionStepResponse> stepResponses = mission.getSteps().stream()
                .map(MissionStepResponse::toResponse)
                .collect(Collectors.toList());

        return MissionResponse.builder()
                .id(mission.getId())
                .sideJobId(mission.getSideJobId())
                .title(mission.getTitle())
                .status(mission.getStatus())
                .orderNo(mission.getOrderNo())
                .designNotes(mission.getDesignNotes())
                .steps(stepResponses)
                .build();
    }
}