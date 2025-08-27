package com.booquest.booquest_api.adapter.in.onboarding.web.dto;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionDetailResponseDto;
import com.booquest.booquest_api.adapter.in.mission.dto.MissionResponseDto;
import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record SideJobDetailResponseDto(
        Long id,
        String title,
        String description,
        List<MissionDetailResponseDto> missions
) {
    public static SideJobDetailResponseDto fromEntity(SideJob entity) {
        if (entity == null) {
            return null;
        }

        var missionDtos =
                (entity.getMissions() == null ? List.<Mission>of() : entity.getMissions())
                        .stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(Mission::getOrderNo))
                        .map(MissionDetailResponseDto::fromEntity)
                        .filter(Objects::nonNull)
                        .toList();

        return new SideJobDetailResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                missionDtos
        );
    }
}
