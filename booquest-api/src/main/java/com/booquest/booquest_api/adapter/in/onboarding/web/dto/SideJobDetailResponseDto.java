package com.booquest.booquest_api.adapter.in.onboarding.web.dto;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionDetailResponseDto;
import com.booquest.booquest_api.adapter.in.mission.dto.MissionResponseDto;
import com.booquest.booquest_api.adapter.in.mission.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;

import java.util.List;

public record SideJobDetailResponseDto(
        Long id,
        String title,
        String description,
        List<MissionDetailResponseDto> missions
) {
    public static SideJobDetailResponseDto fromEntity(SideJob entity) {
        return new SideJobDetailResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getMissions().stream()
                        .map(MissionDetailResponseDto::fromEntity)
                        .toList()
        );
    }
}
