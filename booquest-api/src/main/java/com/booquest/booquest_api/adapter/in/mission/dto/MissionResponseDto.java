package com.booquest.booquest_api.adapter.in.mission.dto;

import com.booquest.booquest_api.domain.mission.model.Mission;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MissionResponseDto(
        Long id,
        String title,
        int order,
        @JsonProperty("design_notes")
        String designNotes,
        String guide
) {
        public static MissionResponseDto fromEntity(Mission mission) {
                return new MissionResponseDto(
                        mission.getId(), mission.getTitle(), mission.getOrderNo(), mission.getDesignNotes(),
                        mission.getGuide()
                );
        }
}
