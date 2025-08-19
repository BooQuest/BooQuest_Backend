package com.booquest.booquest_api.application.port.in.onboarding;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingProgressInfo;

public interface CheckSideJobStatusUseCase {
    boolean isSideJobRecommended(Long userId); //부업 추천여부

    boolean isMissionRecommended(Long userId);// 미션 추천 여부

    boolean isSideJobCreated(Long userId); // 부업 생성 완료 여부

    default OnboardingProgressInfo getOnboardingProgress(Long userId) {
        return OnboardingProgressInfo.builder()
                .sideJobRecommended(isSideJobRecommended(userId))
                .missionRecommended(isMissionRecommended(userId))
                .sideJobCreated(isSideJobCreated(userId))
                .build();
    }
}
