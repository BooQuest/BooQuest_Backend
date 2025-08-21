package com.booquest.booquest_api.adapter.out.auth;

import com.booquest.booquest_api.application.port.out.auth.JwtTokenPort;
import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider implements JwtTokenPort {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-ttl-seconds:3600}") // 1 hour
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-ttl-seconds:2592000}") // 30 days
    private long refreshTokenExpiration;

    @Value("${jwt.access-test-token-ttl-seconds:21600}") // 6 hours
    private long accessTestTokenExpiration;

    @Override
    public TokenInfo generateToken(Long userId, String email) {
        Date now = new Date();
        long accessTtlMs = accessTokenExpiration * 1000L;
        long refreshTtlMs = refreshTokenExpiration * 1000L;
        Date accessExpiryDate = new Date(now.getTime() + accessTtlMs);
        Date refreshExpiryDate = new Date(now.getTime() + refreshTtlMs);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        String accessToken = getAccessToken(userId, email, now, accessExpiryDate, key);
        String refreshToken = getRefreshToken(userId, now, refreshExpiryDate, key);

        return TokenInfo.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(accessTokenExpiration)
            .build();
    }

    private String getAccessToken(Long userId, String email, Date now, Date accessExpiryDate, SecretKey key) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("type", "ACCESS")
                .issuedAt(now)
                .expiration(accessExpiryDate)
                .signWith(key)
                .compact();
    }

    private String getRefreshToken(Long userId, Date now, Date refreshExpiryDate, SecretKey key) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "REFRESH")
                .issuedAt(now)
                .expiration(refreshExpiryDate)
                .signWith(key)
                .compact();
    }

    @Override
    public TokenInfo generateTestToken() {
        long fakeUserId = Math.abs(UUID.randomUUID().getMostSignificantBits());
        String fakeEmail = "tester-" + fakeUserId + "@example.test";

        Date now = new Date();
        long accessTestTtlMs = accessTestTokenExpiration * 1000L;
        Date accessExpiryDate = new Date(now.getTime() + accessTestTtlMs);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        String accessToken = getAccessToken(fakeUserId, fakeEmail, now, accessExpiryDate, key);

        return TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(null)
                .tokenType("Bearer")
                .expiresIn(accessTestTokenExpiration)
                .build();
    }

    @Override
    public Jws<Claims> parse(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }
}