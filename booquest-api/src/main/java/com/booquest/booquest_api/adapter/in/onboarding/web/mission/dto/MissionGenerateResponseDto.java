package com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto;

import com.booquest.booquest_api.domain.mission.model.Mission;

public record MissionGenerateResponseDto(
        Long id,
        String title,
        String designNotes
) {
        public static MissionGenerateResponseDto fromEntity(Mission entity) {
                return new MissionGenerateResponseDto(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getDesignNotes()
                );
        }
}