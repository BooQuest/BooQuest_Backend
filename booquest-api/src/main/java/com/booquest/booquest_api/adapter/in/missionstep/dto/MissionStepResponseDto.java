package com.booquest.booquest_api.adapter.in.missionstep.dto;

import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;
import com.fasterxml.jackson.databind.JsonNode;

public record MissionStepResponseDto(
        Long id,
        String title,
        int seq,
        StepStatus status,
        String detail
) {
        public static MissionStepResponseDto fromEntity(MissionStep entity) {
                if (entity == null) return null;
                return new MissionStepResponseDto(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getSeq(),
                        entity.getStatus(),
                        entity.getDetail()
                );
        }
}