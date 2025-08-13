package com.booquest.booquest_api.application.port.in.auth;

import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;

public interface SocialLoginUseCase {
    SocialLoginResponse login(String accessToken);
}
