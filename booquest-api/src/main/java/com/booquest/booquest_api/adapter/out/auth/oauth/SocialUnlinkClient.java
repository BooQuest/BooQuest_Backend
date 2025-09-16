package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.application.port.out.auth.KakaoUnlinkPort;
import com.booquest.booquest_api.application.port.out.auth.NaverUnlinkPort;
import com.booquest.booquest_api.application.port.out.auth.SocialUnlinkPort;
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
            case APPLE -> {
                // Apple은 서버 측에서 unlink 불가 → 단순 성공 처리
                log.info("[SocialUnlink] Apple은 서버에서 unlink 불가. providerUserId={}", providerUserId);
                return true;
            }
            default -> {
                log.info("[SocialUnlink] Unsupported provider={}, treat as success", provider);
                return true;
            }
        }
    }
}
