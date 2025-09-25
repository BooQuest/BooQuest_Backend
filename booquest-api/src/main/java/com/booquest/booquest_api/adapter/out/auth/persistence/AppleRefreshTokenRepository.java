package com.booquest.booquest_api.adapter.out.auth.persistence;

import com.booquest.booquest_api.domain.auth.model.AppleRefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppleRefreshTokenRepository extends JpaRepository<AppleRefreshToken, Long> {
    @Query("SELECT a.refreshToken FROM AppleRefreshToken a WHERE a.userId = :userId")
    Optional<String> findByUserId(Long userId);
}
