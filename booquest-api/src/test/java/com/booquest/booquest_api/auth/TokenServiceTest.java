package com.booquest.booquest_api.auth;

import com.booquest.booquest_api.application.port.out.auth.JwtTokenPort;
import com.booquest.booquest_api.adapter.out.auth.persistence.jpa.TokenRepository;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenInfo;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenRefreshResponse;
import com.booquest.booquest_api.application.port.out.user.UserQueryPort;
import com.booquest.booquest_api.application.service.auth.TokenService;
import com.booquest.booquest_api.common.exception.TokenException;
import com.booquest.booquest_api.domain.auth.model.Token;
import com.booquest.booquest_api.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

//    @Mock
//    private JwtTokenPort jwtTokenPort;
//
//    @Mock
//    private TokenRepository tokenRepository;
//
//    @Mock
//    private UserQueryPort userQueryPort;
//
//    @InjectMocks
//    private TokenService tokenService;
//
//    private User testUser;
//    private TokenInfo testTokenInfo;
//    private Token testToken;
//
//    @BeforeEach
//    void setUp() {
//        testUser = User.builder()
//                .id(1L)
//                .email("test@example.com")
//                .nickname("testuser")
//                .provider("kakao")
//                .providerUserId("kakao-123")
//                .build();
//
//        testTokenInfo = TokenInfo.builder()
//                .accessToken("test-access-token")
//                .refreshToken("test-refresh-token")
//                .tokenType("Bearer")
//                .expiresIn(3600L)
//                .build();
//
//        testToken = Token.builder()
//                .userId(1L)
//                .refreshToken("test-refresh-token")
////                .accessToken("test-access-token")
//                .expiresAt(LocalDateTime.now().plusHours(1))
//                .build();
//    }
//
//    @Test
//    void issueToken_ShouldSaveTokenToDatabase() {
//        // Given
//        when(jwtTokenPort.generateToken(anyLong(), anyString())).thenReturn(testTokenInfo);
//        when(tokenRepository.findByUserId(anyLong())).thenReturn(Optional.empty());
//        when(tokenRepository.save(any(Token.class))).thenReturn(testToken);
//
//        // When
//        TokenInfo result = tokenService.issueToken(testUser);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(testTokenInfo.getAccessToken(), result.getAccessToken());
//        assertEquals(testTokenInfo.getRefreshToken(), result.getRefreshToken());
//
//        verify(tokenRepository).findByUserId(testUser.getId());
//        verify(tokenRepository).save(any(Token.class));
//    }
//
//    @Test
//    void refreshAccessToken_WithValidToken_ShouldReturnNewAccessToken() {
//        // Given
//        String refreshToken = "valid-refresh-token";
//        TokenRefreshResponse expectedResponse = TokenRefreshResponse.builder()
//                .accessToken("new-access-token")
//                .tokenType("Bearer")
//                .expiresIn(3600L)
//                .build();
//
//        when(tokenRepository.findByRefreshTokenHash(refreshToken)).thenReturn(Optional.of(testToken));
//        when(userQueryPort.findById(testUser.getId())).thenReturn(Optional.of(testUser));
//        when(jwtTokenPort.generateAccessToken(anyLong(), anyString())).thenReturn(expectedResponse);
//        when(tokenRepository.save(any(Token.class))).thenReturn(testToken);
//
//        // When
//        TokenRefreshResponse result = tokenService.refreshAccessToken(refreshToken);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(expectedResponse.getAccessToken(), result.getAccessToken());
//
//        verify(tokenRepository).findByRefreshTokenHash(refreshToken);
//        verify(userQueryPort).findById(testUser.getId());
//        verify(jwtTokenPort).generateAccessToken(testUser.getId(), testUser.getEmail());
//        verify(tokenRepository).save(any(Token.class));
//    }
//
//    @Test
//    void refreshAccessToken_WithInvalidToken_ShouldThrowException() {
//        // Given
//        String invalidRefreshToken = "invalid-refresh-token";
//        when(tokenRepository.findByRefreshTokenHash(invalidRefreshToken)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(TokenException.class, () -> tokenService.refreshAccessToken(invalidRefreshToken));
//        verify(tokenRepository).findByRefreshTokenHash(invalidRefreshToken);
//    }
//
//    @Test
//    void refreshAccessToken_WithExpiredToken_ShouldThrowException() {
//        // Given
//        String refreshToken = "expired-refresh-token";
//        Token expiredToken = Token.builder()
//                .userId(1L)
//                .refreshToken(refreshToken)
////                .accessToken("old-access-token")
//                .expiresAt(LocalDateTime.now().minusHours(1)) // 만료된 토큰
//                .build();
//
//        when(tokenRepository.findByRefreshTokenHash(refreshToken)).thenReturn(Optional.of(expiredToken));
//
//        // When & Then
//        assertThrows(TokenException.class, () -> tokenService.refreshAccessToken(refreshToken));
//        verify(tokenRepository).findByRefreshTokenHash(refreshToken);
//    }
} 