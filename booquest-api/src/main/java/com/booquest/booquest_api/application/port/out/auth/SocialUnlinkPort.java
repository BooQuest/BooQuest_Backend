package com.booquest.booquest_api.application.port.out.auth;

import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import jakarta.annotation.Nullable;

public interface SocialUnlinkPort {
    // providerAccessToken: NAVER 필수, KAKAO는 null 가능
    boolean unlink(AuthProvider provider, String providerUserId, @Nullable String providerAccessToken);
}
