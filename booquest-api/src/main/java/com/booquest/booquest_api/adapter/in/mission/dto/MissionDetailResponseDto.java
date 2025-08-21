package com.booquest.booquest_api.adapter.in.mission.dto;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public record MissionDetailResponseDto(
        Long id,
        String title,
        int order,
        String designNotes,
        List<MissionStepResponseDto> missionSteps
) {
        public static MissionDetailResponseDto fromEntity(Mission entity) {
                return new MissionDetailResponseDto(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getOrderNo(),
                        entity.getDesignNotes(),
                        entity.getSteps().stream()
                                .map(MissionStepResponseDto::fromEntity)
                                .toList()
                );
        }
}