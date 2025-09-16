package com.booquest.booquest_api.domain.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apple_refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleRefreshToken {

    @Id
    private Long userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String refreshToken;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Builder
    public AppleRefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.issuedAt = LocalDateTime.now();
    }

}
