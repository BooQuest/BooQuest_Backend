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

    private final TokenRepository tokenJpaRepository;

    @Override
    public Token save(Token token) {
        return tokenJpaRepository.save(token);
    }

    @Override
    public Optional<Token> findByRefreshTokenHash(String refreshTokenHash) {
        return tokenJpaRepository.findByRefreshToken(refreshTokenHash);
    }

    @Override
    public Optional<Token> findByUserId(Long userId) {
        return tokenJpaRepository.findByUserId(userId);
    }

    @Override
    public long deleteByUserId(Long userId) {
        return tokenJpaRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByRefreshTokenHash(String refreshTokenHash) {
        tokenJpaRepository.deleteByRefreshToken(refreshTokenHash);
    }

    @Override
    public int upsertByUserId(Long userId, String refreshTokenHash, LocalDateTime expiresAt) {
        return tokenJpaRepository.upsertByUserId(userId, refreshTokenHash, java.sql.Timestamp.valueOf(expiresAt));
    }
}