package com.booquest.booquest_api.application.port.in.auth;

import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenInfo;
import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenRefreshResponse;
import com.booquest.booquest_api.domain.user.model.User;

public interface TokenUseCase {
    TokenInfo issueToken(User user);
    TokenInfo issueTestToken();
    TokenRefreshResponse refreshAccessToken(String refreshToken);
}
