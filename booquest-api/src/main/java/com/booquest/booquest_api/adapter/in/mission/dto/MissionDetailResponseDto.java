package com.booquest.booquest_api.adapter.in.mission.dto;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Objects;

public record MissionDetailResponseDto(
        Long id,
        String title,
        int order,
        String designNotes,
        List<MissionStepResponseDto> missionSteps
) {

        public static MissionDetailResponseDto fromEntity(Mission entity) {
                if (entity == null) return null;

                var steps = entity.getSteps() == null ? List.<MissionStep>of() : entity.getSteps();

                var stepDtos = steps.stream()
                        .filter(Objects::nonNull)
                        .map(MissionStepResponseDto::fromEntity)
                        .filter(Objects::nonNull)
                        .toList();

                return new MissionDetailResponseDto(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getOrderNo(),
                        entity.getDesignNotes(),
                        stepDtos
                );
        }
}