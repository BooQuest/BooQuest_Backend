package com.booquest.booquest_api.application.port.in.auth;

import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;

public interface SocialLoginUseCase {
    SocialLoginResponse login(String accessToken, AuthProvider provider);
}
