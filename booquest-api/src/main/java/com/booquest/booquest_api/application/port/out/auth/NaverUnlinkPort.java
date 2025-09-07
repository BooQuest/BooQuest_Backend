package com.booquest.booquest_api.application.port.out.auth;

public interface NaverUnlinkPort {
    // 네이버는 access token 필수
    boolean unlinkByAccessToken(String accessToken);
}
