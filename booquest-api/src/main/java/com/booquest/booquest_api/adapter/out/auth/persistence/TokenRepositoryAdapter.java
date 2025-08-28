package com.booquest.booquest_api.adapter.out.auth.persistence;

import com.booquest.booquest_api.application.port.out.auth.TokenRepositoryPort;
import com.booquest.booquest_api.domain.auth.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepositoryPort {

    private final TokenRepository tokenRepository;

    @Override
    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Optional<Token> findByRefreshTokenHash(String refreshTokenHash) {
        return tokenRepository.findByRefreshToken(refreshTokenHash);
    }

    @Override
    public Optional<Token> findByUserId(Long userId) {
        return tokenRepository.findByUserId(userId);
    }

    @Override
    public long deleteByUserId(Long userId) {
        return tokenRepository.deleteByUserId(userId);
    }

    @Override
    public int deleteByRefreshTokenHash(String refreshTokenHash) {
        return tokenRepository.deleteByRefreshToken(refreshTokenHash);
    }

    @Override
    public int upsertByUserId(Long userId, String refreshTokenHash, LocalDateTime expiresAt) {
        return tokenRepository.upsertByUserId(userId, refreshTokenHash, java.sql.Timestamp.valueOf(expiresAt));
    }
}