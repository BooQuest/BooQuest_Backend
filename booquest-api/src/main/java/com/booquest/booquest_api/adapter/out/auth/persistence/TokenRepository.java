package com.booquest.booquest_api.adapter.out.auth.persistence;

import com.booquest.booquest_api.domain.auth.model.Token;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Token> findByRefreshToken(String refreshToken);

    Optional<Token> findByUserId(Long userId);

    long deleteByUserId(Long userId);

    int deleteByRefreshToken(String refreshToken);

    @Modifying
    @Query(value = """
    INSERT INTO tokens (user_id, refresh_token, expires_at, is_revoked, created_at, updated_at)
    VALUES (:userId, :refreshTokenHash, :expiresAt, false, NOW(), NOW())
    ON CONFLICT (user_id)
    DO UPDATE SET
      refresh_token = EXCLUDED.refresh_token,
      expires_at    = EXCLUDED.expires_at,
      is_revoked    = false,
      updated_at    = NOW();
    """, nativeQuery = true)
    int upsertByUserId(@Param("userId") Long userId,
                       @Param("refreshTokenHash") String refreshTokenHash,
                       @Param("expiresAt") java.sql.Timestamp expiresAt);
}