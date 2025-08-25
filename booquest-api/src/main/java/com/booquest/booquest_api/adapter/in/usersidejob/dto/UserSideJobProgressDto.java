package com.booquest.booquest_api.adapter.in.usersidejob.dto;

import lombok.Builder;

@Builder
public record UserSideJobProgressDto(
        int percent,                            // 부업 전체 진행률 (모든 미션의 스텝 기준)
        StageProgress stage,                    // 단계(미션) 요약
        CurrentMissionProgress currentMission   // 현재 단계 상세
) {}
