package com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto;

import com.booquest.booquest_api.domain.mission.model.Mission;


public record MissionResponseDto(
        Long id,
        String title,
        int order,
        String designNotes
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