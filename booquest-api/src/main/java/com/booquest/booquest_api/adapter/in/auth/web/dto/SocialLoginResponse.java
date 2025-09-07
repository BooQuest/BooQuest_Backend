package com.booquest.booquest_api.adapter.in.auth.web.dto;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingProgressInfo;
import com.booquest.booquest_api.domain.character.enums.CharacterType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginResponse {
    private TokenInfo tokenInfo;
    private UserInfo userInfo;
    private OnboardingProgressInfo onboardingProgressInfo;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String email;
        private String nickname;
        private String profileImageUrl;
        private CharacterType characterType;
    }

    public static SocialLoginResponse of(TokenInfo tokenInfo, UserInfo userInfo,
                                         OnboardingProgressInfo onboardingProgressInfo) {
        return SocialLoginResponse.builder()
                .tokenInfo(tokenInfo)
                .userInfo(userInfo)
                .onboardingProgressInfo(onboardingProgressInfo)
                .build();
    }
}
