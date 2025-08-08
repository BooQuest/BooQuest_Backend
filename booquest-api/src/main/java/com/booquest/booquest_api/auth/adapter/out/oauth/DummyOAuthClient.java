package com.booquest.booquest_api.auth.adapter.out.oauth;

import com.booquest.booquest_api.auth.application.port.out.OAuthClientPort;
import com.booquest.booquest_api.auth.domain.model.SocialUser;
import org.springframework.stereotype.Component;

@Component
public class DummyOAuthClient implements OAuthClientPort {

    @Override
    public SocialUser fetchUserInfo(String accessToken) {
        // accessToken은 무시하고 그냥 더미 데이터 반환
        if (accessToken.equals("exist-user"))
            return new SocialUser("test@booquest.com", "회원가입한 유저", "KAKAO", "exist-user");
        return new SocialUser("test@booquest.com", "회원가입하지 않은 유저", "KAKAO", "not-exist-user");
    }
}
