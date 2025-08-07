package com.booquest.booquest_api.auth.application.port.in;

import com.booquest.booquest_api.auth.adapter.in.web.dto.SocialLoginResponse;

public interface SocialLoginUseCase {
    SocialLoginResponse login(String accessToken);
}
