package com.booquest.booquest_api.application.service.auth;

import com.booquest.booquest_api.adapter.in.auth.web.dto.LogoutResponse;
import com.booquest.booquest_api.application.port.in.auth.LogoutUseCase;
import com.booquest.booquest_api.application.port.out.auth.TokenHashingPort;
import com.booquest.booquest_api.application.port.out.auth.TokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogoutService implements LogoutUseCase {
    private final TokenRepositoryPort tokenRepositoryPort;
    private final TokenHashingPort tokenHashingPort;

    @Override
    public LogoutResponse logoutCurrentSession(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException("X-Refresh-Token is required");
        }

        String refreshTokenHash = tokenHashingPort.sha256Base64(refreshToken);
        int deleted = tokenRepositoryPort.deleteByRefreshTokenHash(refreshTokenHash);

        return new LogoutResponse(true, "current", deleted);
    }

    @Override
    public LogoutResponse logoutAllDevices(Long userId) {
        int deleted = (int) tokenRepositoryPort.deleteByUserId(userId);
        return new LogoutResponse(true, "all", deleted);
    }
}
