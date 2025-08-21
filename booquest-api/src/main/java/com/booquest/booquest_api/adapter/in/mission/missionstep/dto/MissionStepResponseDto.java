package com.booquest.booquest_api.adapter.in.mission.missionstep.dto;

import com.booquest.booquest_api.domain.mission.model.MissionStep;
import com.booquest.booquest_api.domain.sidejob.enums.StepStatus;
import com.fasterxml.jackson.databind.JsonNode;

public record MissionStepResponseDto(
        Long id,
        String title,
        int seq,
        StepStatus status,
        String detail
) {
        public static MissionStepResponseDto fromEntity(MissionStep entity) {
                return new MissionStepResponseDto(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getSeq(),
                        entity.getStatus(),
                        entity.getDetail()
                );
        }
}