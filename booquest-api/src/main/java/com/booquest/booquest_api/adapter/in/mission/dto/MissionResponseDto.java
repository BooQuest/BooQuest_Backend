package com.booquest.booquest_api.adapter.in.mission.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MissionResponseDto(
        Long id,
        String title,
        int order,
        @JsonProperty("design_notes")
        String designNotes,
        String guide
) {}
