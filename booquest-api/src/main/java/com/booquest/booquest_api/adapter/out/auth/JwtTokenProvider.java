package com.booquest.booquest_api.adapter.out.auth;

import com.booquest.booquest_api.application.port.out.auth.JwtTokenPort;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider implements JwtTokenPort {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-ttl-seconds:3600}") // 1 hour
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-ttl-seconds:2592000}") // 30 days
    private long refreshTokenExpiration;

    @Override
    public TokenInfo generateToken(Long userId, String email) {
        Date now = new Date();
        Date accessExpiryDate = new Date(now.getTime() + accessTokenExpiration);
        Date refreshExpiryDate = new Date(now.getTime() + refreshTokenExpiration);

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        String accessToken = Jwts.builder()
            .subject(String.valueOf(userId))
            .claim("email", email)
            .claim("type", "ACCESS")
            .issuedAt(now)
            .expiration(accessExpiryDate)
            .signWith(key)
            .compact();

        String refreshToken = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "REFRESH")
                .issuedAt(now)
                .expiration(refreshExpiryDate)
                .signWith(key)
                .compact();

        return TokenInfo.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(accessTokenExpiration / 1000) // Convert to seconds
            .build();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            return Long.parseLong(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Failed to get user ID from token: {}", e.getMessage());
            throw new RuntimeException("Invalid token", e);
        }
    }
}