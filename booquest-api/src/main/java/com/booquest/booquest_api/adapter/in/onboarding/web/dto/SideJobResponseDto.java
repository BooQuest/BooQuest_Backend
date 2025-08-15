package com.booquest.booquest_api.adapter.in.onboarding.web.dto;

import com.booquest.booquest_api.domain.sidejob.model.SideJob;

public record SideJobResponseDto(
        Long id,
        String title,
        String description
) {
    public static SideJobResponseDto fromEntity(SideJob entity) {
        return new SideJobResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription()
        );
    }
}
