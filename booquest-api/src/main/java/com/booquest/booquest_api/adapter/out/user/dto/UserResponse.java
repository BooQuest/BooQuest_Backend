package com.booquest.booquest_api.adapter.out.user.dto;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingProgressInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String provider;
    private String providerUserId;
    private String email;
    private String nickname;
    private String socialNickname;
    private String profileImageUrl;
    private OnboardingProgressInfo onboardingProgressInfo;

    public static UserResponse of(UserResponse userResponse, OnboardingProgressInfo onboardingProgressInfo) {
        return UserResponse.builder()
                .id(userResponse.getId())
                .provider(userResponse.getProvider())
                .providerUserId(userResponse.getProviderUserId())
                .email(userResponse.getEmail())
                .nickname(userResponse.getNickname())
                .socialNickname(userResponse.getSocialNickname())
                .profileImageUrl(userResponse.getProfileImageUrl())
                .onboardingProgressInfo(onboardingProgressInfo)
                .build();
    }
}
