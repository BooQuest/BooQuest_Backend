package com.booquest.booquest_api.adapter.out.auth.persistence.jpa;

import com.booquest.booquest_api.domain.auth.model.Token;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository {
    Token save(Token token);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Token> findByRefreshTokenHash(String refreshTokenHash);
    Optional<Token> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    void deleteByRefreshTokenHash(String refreshTokenHash);
    int upsertByUserId(Long userId, String refreshTokenHash, LocalDateTime expiresAt);
}