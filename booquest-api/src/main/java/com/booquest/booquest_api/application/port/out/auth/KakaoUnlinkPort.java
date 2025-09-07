package com.booquest.booquest_api.application.port.out.auth;

public interface KakaoUnlinkPort {
    // Admin Key + kakao user id 로 강제 unlink
    boolean unlinkByUserId(String kakaoUserId);

    // 사용자 access token 로 unlink (선택)
    boolean unlinkByAccessToken(String accessToken);
}
