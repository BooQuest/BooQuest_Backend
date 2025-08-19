package com.booquest.booquest_api.application.service.auth;

import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.application.port.out.auth.JwtTokenPort;
import com.booquest.booquest_api.adapter.out.auth.persistence.jpa.TokenRepository;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenInfo;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenRefreshResponse;
import com.booquest.booquest_api.application.port.out.user.UserQueryPort;
import com.booquest.booquest_api.common.exception.TokenException;
import com.booquest.booquest_api.domain.auth.model.Token;
import com.booquest.booquest_api.domain.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
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

    @Override
    public TokenInfo issueToken(User user) {
        TokenInfo tokenInfo = jwtTokenPort.generateToken(user.getId(), user.getEmail());

        // 기존 토큰이 있다면 삭제
//        tokenRepository.findByUserId(user.getId()).ifPresent(token ->
//            tokenRepository.deleteByUserId(user.getId())
//        );

        // 새 토큰을 DB에 저장
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
            new Date(System.currentTimeMillis() + (2592000 * 1000L)).toInstant(),
            ZoneId.systemDefault()
        );

        String newRefreshTokenHash = sha256Base64(tokenInfo.getRefreshToken());
        tokenRepository.upsertByUserId(user.getId(), newRefreshTokenHash, expiresAt);

//        Token token = Token.builder()
//                .userId(user.getId())
//                .refreshToken(newRefreshTokenHash)
//                .expiresAt(expiresAt)
//                .build();
//        tokenRepository.save(token);


        return tokenInfo;
//        return jwtTokenPort.generateToken(user.getId(), user.getEmail());
    }

    @Override
    public TokenInfo issueTestToken() {
        return jwtTokenPort.generateTestToken();
    }

    @Override
    public TokenRefreshResponse refreshAccessToken(String refreshToken) {
        // 리프레시 토큰(hash)으로 DB에서 토큰 정보 조회
        String refreshTokenHash = sha256Base64(refreshToken);
        Token token = tokenRepository.findByRefreshTokenHash(refreshTokenHash)
                .orElseThrow(() -> new TokenException("Invalid refresh token"));

        // 리프레시 토큰 유효성 검사
        if (!token.isValid()) {
            throw new TokenException("Token is expired or revoked");
        }

//        final Claims claims;
//        try {
//            var jws = jwtTokenPort.parse(refreshToken);
//            claims = jws.getPayload();
//            if (!"REFRESH".equals(claims.get("type", String.class))) {
//                throw new TokenException("Invalid token type");
//            }
//        } catch (ExpiredJwtException e) {
//            throw new TokenException("Refresh token expired", e);
//        } catch (JwtException e) {
//            throw new TokenException("Invalid refresh token", e);
//        }
        
        // 사용자 정보 조회 (새로운 액세스 토큰 생성을 위한)
        User user = userQueryPort.findById(token.getUserId())
                .orElseThrow(() -> new TokenException("User not found"));
        
        // 새로운 액세스 토큰 생성
        var newToken = jwtTokenPort.generateToken(token.getUserId(), user.getEmail());
        String newAccessToken = newToken.getAccessToken();
        String newRefreshToken = newToken.getRefreshToken();
        String newRefreshTokenHash = sha256Base64(newRefreshToken);
        
        // DB 업데이트
//        LocalDateTime newAccessExpiresAt = LocalDateTime.ofInstant(
//            new Date(System.currentTimeMillis() + (3600 * 1000L)).toInstant(),
//            ZoneId.systemDefault()
//        );

        LocalDateTime newRefreshExpiresAt = LocalDateTime.ofInstant(
                new Date(System.currentTimeMillis() + (2592000 * 1000L)).toInstant(),
                ZoneId.systemDefault()
        );

        token.updateToken(newRefreshTokenHash, newRefreshExpiresAt);
        tokenRepository.save(token);

        return TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L / 3600)
                .build();
    }

    private static String sha256Base64(String s) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            return java.util.Base64.getEncoder().encodeToString(md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
