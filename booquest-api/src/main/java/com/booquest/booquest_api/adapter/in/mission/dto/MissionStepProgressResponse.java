package com.booquest.booquest_api.adapter.in.mission.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionStepProgressResponse {
    private int percent;              // 0~100
    private int completedStepCount;   // 완료 스텝 수
    private int totalStepCount;       // 전체 스텝 수
    private Long currentStepId;       // 아직 완료 안 된 첫 스텝 id (없으면 null)
    private Integer currentStepOrder; // 그 스텝의 seq/order (없으면 null)
}
