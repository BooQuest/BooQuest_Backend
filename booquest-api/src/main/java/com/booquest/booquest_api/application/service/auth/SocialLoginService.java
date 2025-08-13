package com.booquest.booquest_api.application.service.auth;

import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.application.port.in.auth.SocialLoginUseCase;
import com.booquest.booquest_api.application.port.out.auth.JwtTokenPort;
import com.booquest.booquest_api.application.port.out.auth.OAuthClientPort;
import com.booquest.booquest_api.application.port.out.auth.UserQueryPort;
import com.booquest.booquest_api.domain.user.model.SocialUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SocialLoginService implements SocialLoginUseCase {

    private final OAuthClientPort oAuthClient;
    private final UserQueryPort userQueryPort; // User 정보 확인용
    private final JwtTokenPort jwtTokenPort;   // JWT 생성용

    @Override
    public SocialLoginResponse login(String accessToken) {
        SocialUser socialUser = oAuthClient.fetchUserInfo(accessToken);

        boolean registered = userQueryPort.existsByProviderAndProviderId(socialUser.getProvider(), socialUser.getProviderId());

        if (registered) {
            String jwt = jwtTokenPort.generateToken(socialUser.getProvider(), socialUser.getProviderId());
            return new SocialLoginResponse(true, jwt, socialUser);
        } else {
            return new SocialLoginResponse(false, null, null);
        }
    }
}
