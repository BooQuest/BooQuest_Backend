package com.booquest.booquest_api.application.service.auth;

import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.application.port.out.auth.JwtTokenPort;
import com.booquest.booquest_api.adapter.out.auth.persistence.TokenRepository;
import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenInfo;
import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenRefreshResponse;
import com.booquest.booquest_api.application.port.out.user.UserQueryPort;
import com.booquest.booquest_api.common.exception.TokenException;
import com.booquest.booquest_api.domain.auth.model.Token;
import com.booquest.booquest_api.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService implements TokenUseCase {
    private final JwtTokenPort jwtTokenPort;
    private final TokenRepository tokenRepository;
    private final UserQueryPort userQueryPort;

    @Value("${jwt.access-token-ttl-seconds:3600}") // 1 hour
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-ttl-seconds:2592000}") // 30 days
    private long refreshTokenExpiration;

    @Override
    public TokenInfo issueToken(User user) {
        TokenInfo tokenInfo = jwtTokenPort.generateToken(user.getId(), user.getEmail());

        String newRefreshTokenHash = sha256Base64(tokenInfo.getRefreshToken());

        long refreshTtlMs = refreshTokenExpiration * 1000L;
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
            new Date(System.currentTimeMillis() + refreshTtlMs).toInstant(),
            ZoneId.systemDefault()
        );

        tokenRepository.upsertByUserId(user.getId(), newRefreshTokenHash, expiresAt);

        return tokenInfo;
    }

    @Override
    public TokenInfo issueTestToken() {
        return jwtTokenPort.generateTestToken();
    }

    @Override
    public TokenRefreshResponse refreshAccessToken(String refreshToken) {
        String refreshTokenHash = sha256Base64(refreshToken);
        Token token = tokenRepository.findByRefreshTokenHash(refreshTokenHash)
                .orElseThrow(() -> new TokenException("Invalid refresh token"));

        if (!token.isValid()) {
            throw new TokenException("Token is expired or revoked");
        }

        User user = userQueryPort.findById(token.getUserId())
                .orElseThrow(() -> new TokenException("User not found"));

        var newToken = jwtTokenPort.generateToken(token.getUserId(), user.getEmail());
        String newAccessToken = newToken.getAccessToken();
        String newRefreshToken = newToken.getRefreshToken();
        String newRefreshTokenHash = sha256Base64(newRefreshToken);

        long refreshTtlMs = refreshTokenExpiration * 1000L;
        LocalDateTime newRefreshExpiresAt = LocalDateTime.ofInstant(
                new Date(System.currentTimeMillis() + refreshTtlMs).toInstant(),
                ZoneId.systemDefault()
        );

        token.updateToken(newRefreshTokenHash, newRefreshExpiresAt);
        tokenRepository.save(token);

        return TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenExpiration)
                .build();
    }

    private static String sha256Base64(String s) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            return java.util.Base64.getEncoder().encodeToString(md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
