package com.booquest.booquest_api.application.port.in;

import com.booquest.booquest_api.adapter.in.web.dto.SocialLoginResponse;

public interface SocialLoginUseCase {
    SocialLoginResponse login(String accessToken);
}
