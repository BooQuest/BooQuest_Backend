package com.booquest.booquest_api.auth.application.port.out;

import com.booquest.booquest_api.auth.domain.model.SocialUser;

public interface OAuthClientPort {
    SocialUser fetchUserInfo(String accessToken);
}
