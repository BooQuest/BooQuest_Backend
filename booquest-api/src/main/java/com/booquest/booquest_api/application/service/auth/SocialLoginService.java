package com.booquest.booquest_api.application.service.auth;

import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.adapter.out.auth.oauth.KakaoOAuthClient;
import com.booquest.booquest_api.application.port.in.auth.SocialLoginUseCase;
import com.booquest.booquest_api.application.port.out.auth.*;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenInfo;
import com.booquest.booquest_api.application.port.out.user.UserCommandPort;
import com.booquest.booquest_api.application.port.out.user.UserQueryPort;
import com.booquest.booquest_api.domain.user.model.SocialUser;
import com.booquest.booquest_api.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialLoginService implements SocialLoginUseCase {
//    private final OAuthClientPort oAuthClient;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserQueryPort userQueryPort; // User 정보 확인용
    private final TokenService tokenService;
    private final UserCommandPort userCommandPort;

    @Override
    public SocialLoginResponse login(String accessToken, String provider) {
        SocialUser socialUser = fetchSocialUser(accessToken, provider);

        User user = findExistingUser(provider, socialUser.getProviderId())
                .map(u -> updateExistingUserProfile(u, socialUser))
                .orElseGet(() -> registerNewUser(provider, socialUser));

        TokenInfo tokenInfo = tokenService.issueToken(user);

        return buildResponse(tokenInfo, user);
    }

    private SocialUser fetchSocialUser(String accessToken, String provider) {
//        return oAuthClient.fetchUserInfo(accessToken, provider);
        if ("KAKAO".equalsIgnoreCase(provider)) {
            return kakaoOAuthClient.fetchUserInfo(accessToken);
        }
        throw new IllegalArgumentException("Unsupported provider: " + provider);
    }

    private Optional<User> findExistingUser(String provider, String providerUserId) {
        return userQueryPort.findByProviderAndProviderUserId(provider, providerUserId);
    }

    private User updateExistingUserProfile(User user, SocialUser socialUser) {
        user.updateProfile(socialUser.getNickname(), socialUser.getProfileImageUrl());
        return userCommandPort.update(user);
    }

    private User registerNewUser(String provider, SocialUser socialUser) {
        User user = User.builder()
                .provider(provider)
                .providerUserId(socialUser.getProviderId())
                .email(socialUser.getEmail())
                .nickname(socialUser.getNickname())          // 앱 표시 닉네임 초기값
                .socialNickname(socialUser.getNickname())    // 소셜 로그인 닉네임
                .profileImageUrl(socialUser.getProfileImageUrl())
                .build();
        return userCommandPort.save(user);
    }

    private SocialLoginResponse buildResponse(TokenInfo tokenInfo, User user) {
        return SocialLoginResponse.builder()
                .tokenInfo(SocialLoginResponse.TokenInfo.builder()
                        .accessToken(tokenInfo.getAccessToken())
                        .refreshToken(tokenInfo.getRefreshToken())
                        .tokenType(tokenInfo.getTokenType())
                        .expiresIn(tokenInfo.getExpiresIn())
                        .build())
                .userInfo(SocialLoginResponse.UserInfo.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .profileImageUrl(user.getProfileImageUrl())
                        .build())
                .build();
    }
}
