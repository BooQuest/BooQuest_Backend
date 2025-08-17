package com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto;

import com.booquest.booquest_api.adapter.in.onboarding.web.mission.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.mission.model.MissionStep;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record MissionResponseDto(
        Long id,
        String title,
        int order,
        JsonNode designNotes
) {
        public static MissionResponseDto fromEntity(Mission entity) {
                return new MissionResponseDto(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getOrderNo(),
                        entity.getDesignNotes()
                );
        }
}