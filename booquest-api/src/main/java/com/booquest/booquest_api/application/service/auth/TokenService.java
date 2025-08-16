package com.booquest.booquest_api.application.service.auth;

import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.application.port.out.auth.JwtTokenPort;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenInfo;
import com.booquest.booquest_api.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService implements TokenUseCase {
    private final JwtTokenPort jwtTokenPort;

    @Override
    public TokenInfo issueToken(User user) {
        return jwtTokenPort.generateToken(user.getId(), user.getEmail());
    }

    @Override
    public TokenInfo issueTestToken() {
        return jwtTokenPort.generateTestToken();
    }
}
