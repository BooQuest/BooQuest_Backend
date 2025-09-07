package com.booquest.booquest_api.adapter.in.user.web.dto;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingProgressInfo;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import com.booquest.booquest_api.domain.character.enums.CharacterType;
import com.booquest.booquest_api.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private AuthProvider provider;
    private String providerUserId;
    private String email;
    private String nickname;
    private String socialNickname;
    private String profileImageUrl;
    private OnboardingProgressInfo onboardingProgressInfo;
    private CharacterType characterType;

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .provider(user.getProvider())
                .providerUserId(user.getProviderUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .socialNickname(user.getSocialNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

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
                .characterType(userResponse.getCharacterType())
                .build();
    }

    public static UserResponse withCharacterType(UserResponse base, CharacterType type) {
        return UserResponse.builder()
                .id(base.getId())
                .provider(base.getProvider())
                .providerUserId(base.getProviderUserId())
                .email(base.getEmail())
                .nickname(base.getNickname())
                .socialNickname(base.getSocialNickname())
                .profileImageUrl(base.getProfileImageUrl())
                .onboardingProgressInfo(base.getOnboardingProgressInfo())
                .characterType(type)
                .build();
    }
}
