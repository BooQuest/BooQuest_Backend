package com.booquest.booquest_api.auth.application.port.in;

import com.booquest.booquest_api.auth.domain.model.SocialUser;

public interface SocialLoginUseCase {
    SocialUser login(String accessToken);
}
