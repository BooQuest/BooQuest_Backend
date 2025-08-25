package com.booquest.booquest_api.adapter.in.usersidejob.dto;

import lombok.Builder;

@Builder
public record StageProgress(
        int total,                   // 전체 단계 수(=미션 개수)
        Integer currentOrder,        // 현재 진행 중인 단계 번호(없으면 null=모두 완료)
        int remaining                // 목표까지 남은 단계 수 (total - currentOrder; null이면 0)
) {}
