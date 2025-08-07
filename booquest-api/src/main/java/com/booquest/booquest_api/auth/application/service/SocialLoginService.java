package com.booquest.booquest_api.auth.application.service;

import com.booquest.booquest_api.auth.application.port.in.SocialLoginUseCase;
import com.booquest.booquest_api.auth.application.port.out.OAuthClientPort;
import com.booquest.booquest_api.auth.domain.model.SocialUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SocialLoginService implements SocialLoginUseCase {

    private final OAuthClientPort oAuthClient;

    @Override
    public SocialUser login(String accessToken) {
        return oAuthClient.fetchUserInfo(accessToken);
    }
}
