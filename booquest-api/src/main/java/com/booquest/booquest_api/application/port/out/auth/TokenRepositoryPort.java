package com.booquest.booquest_api.application.port.out.auth;

import com.booquest.booquest_api.domain.auth.model.AppleRefreshToken;
import com.booquest.booquest_api.domain.auth.model.Token;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepositoryPort {
    Token save(Token token);

    Optional<Token> findByRefreshTokenHash(String refreshTokenHash);

    Optional<Token> findByUserId(Long userId);

    long deleteByUserId(Long userId);

    int deleteByRefreshTokenHash(String refreshTokenHash);

    int upsertByUserId(Long userId, String refreshTokenHash, LocalDateTime expiresAt);

    void saveAppleRefreshToken(AppleRefreshToken appleRefreshToken);

    Optional<String> findRefreshTokenByUserId(Long userId);
}
