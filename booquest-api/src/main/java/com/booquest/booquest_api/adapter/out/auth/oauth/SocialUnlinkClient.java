package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.application.port.out.auth.AppleUnlinkPort;
import com.booquest.booquest_api.application.port.out.auth.KakaoUnlinkPort;
import com.booquest.booquest_api.application.port.out.auth.NaverUnlinkPort;
import com.booquest.booquest_api.application.port.out.auth.SocialUnlinkPort;
import com.booquest.booquest_api.application.port.out.auth.TokenRepositoryPort;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocialUnlinkClient implements SocialUnlinkPort {
    private final KakaoUnlinkPort kakaoUnlinkPort;
    private final NaverUnlinkPort naverUnlinkPort;
    private final AppleUnlinkPort appleUnlinkPort;
    private final TokenRepositoryPort tokenRepositoryPort;

    @Override
    public boolean unlink(AuthProvider provider, String providerUserId, @Nullable String providerAccessToken) {
        switch (provider) {
            case KAKAO -> {
                // 1순위: Admin Key + userId 방식 (액세스 토큰 불필요)
                boolean ok = kakaoUnlinkPort.unlinkByUserId(providerUserId);
                // (선택) 실패했고 액세스 토큰이 있다면 Bearer 방식으로 한 번 더 시도 가능
                if (!ok && providerAccessToken != null && !providerAccessToken.isBlank()) {
                    log.warn("[SocialUnlink] Kakao AdminKey unlink failed, try Bearer token fallback");
                    ok = kakaoUnlinkPort.unlinkByAccessToken(providerAccessToken);
                }
                return ok;
            }
            case NAVER -> {
                // NAVER는 access token이 반드시 필요
                if (providerAccessToken == null || providerAccessToken.isBlank()) {
                    log.warn("[SocialUnlink] NAVER requires access token for unlink. providerUserId={}", providerUserId);
                    return false;
                }
                return naverUnlinkPort.unlinkByAccessToken(providerAccessToken);
            }
            default -> {
                log.info("[SocialUnlink] Unsupported provider={}, treat as success", provider);
                return true;
            }
        }
    }

    @Override
    public boolean unlinkApple(Long userId) {
        // refreshToken이 유효할 때 처리
        return tokenRepositoryPort.findRefreshTokenByUserId(userId)
                .filter(token -> !token.isBlank())
                .map(appleUnlinkPort::revokeToken)
                .orElseGet(() -> {
                    log.warn("[SocialUnlink] Apple refresh_token not found or blank for userId={}", userId);
                    return false;
                });
    }
}
