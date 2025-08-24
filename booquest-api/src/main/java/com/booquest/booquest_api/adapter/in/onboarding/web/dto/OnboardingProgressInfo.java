package com.booquest.booquest_api.adapter.in.onboarding.web.dto;

import lombok.Builder;

@Builder
public record OnboardingProgressInfo(boolean sideJobRecommended, boolean missionRecommended, boolean sideJobCreated, Long selectedSideJobId) {
}
