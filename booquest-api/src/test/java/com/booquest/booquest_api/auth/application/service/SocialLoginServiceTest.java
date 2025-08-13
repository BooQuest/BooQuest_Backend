package com.booquest.booquest_api.auth.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.booquest.booquest_api.adapter.in.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.application.port.in.SocialLoginUseCase;
import com.booquest.booquest_api.application.port.out.JwtTokenPort;
import com.booquest.booquest_api.application.port.out.OAuthClientPort;
import com.booquest.booquest_api.application.port.out.UserQueryPort;
import com.booquest.booquest_api.application.service.SocialLoginService;
import com.booquest.booquest_api.domain.user.model.SocialUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SocialLoginServiceTest {

    private OAuthClientPort oAuthClient;
    private UserQueryPort userQueryPort;
    private JwtTokenPort jwtTokenPort;
    private SocialLoginUseCase socialLoginService;

    @BeforeEach
    void setUp() {
        oAuthClient = mock(OAuthClientPort.class);
        userQueryPort = mock(UserQueryPort.class);
        jwtTokenPort = mock(JwtTokenPort.class);
        socialLoginService = new SocialLoginService(oAuthClient, userQueryPort, jwtTokenPort);
    }

    @Test
    @DisplayName("이미 회원가입된 유저라면 jwt 토큰과 회원 정보를 리턴한다")
    void login_with_registered_user_returns_jwt_and_user() {
        // given
        String accessToken = "dummy-token";
        SocialUser dummyUser = new SocialUser("dummy@example.com", "testName", "KAKAO", "KAKAO123123");
        when(oAuthClient.fetchUserInfo(accessToken)).thenReturn(dummyUser);
        when(userQueryPort.existsByProviderAndProviderId("KAKAO", "KAKAO123123")).thenReturn(true);
        when(jwtTokenPort.generateToken("KAKAO", "KAKAO123123")).thenReturn("jwt-token");

        // when
        SocialLoginResponse response = socialLoginService.login(accessToken);

        // then
        assertThat(response.registered()).isTrue();
        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.userInfo()).isEqualTo(dummyUser);
    }

    @Test
    @DisplayName("회원가입되지 않은 유저하면 registered 값을 false로 응답한다")
    void login_with_unregistered_user_returns_nulls() {
        // given
        String accessToken = "dummy-token";
        SocialUser dummyUser = new SocialUser("dummy@example.com", "testName", "KAKAO", "KAKAO123123");
        when(oAuthClient.fetchUserInfo(accessToken)).thenReturn(dummyUser);
        when(userQueryPort.existsByProviderAndProviderId("KAKAO", "KAKAO123123")).thenReturn(false);

        // when
        SocialLoginResponse response = socialLoginService.login(accessToken);

        // then
        assertThat(response.registered()).isFalse();
        assertThat(response.token()).isNull();
        assertThat(response.userInfo()).isNull();
    }
}
