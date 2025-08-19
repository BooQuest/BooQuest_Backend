package com.booquest.booquest_api.application.port.in.auth;

import com.booquest.booquest_api.application.port.out.auth.dto.TokenInfo;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenRefreshResponse;
import com.booquest.booquest_api.domain.user.model.User;

public interface TokenUseCase {
    TokenInfo issueToken(User user);
    TokenInfo issueTestToken();
    TokenRefreshResponse refreshAccessToken(String refreshToken);
}
