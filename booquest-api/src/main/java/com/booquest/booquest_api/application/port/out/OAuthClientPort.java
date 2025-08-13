package com.booquest.booquest_api.application.port.out;

import com.booquest.booquest_api.domain.user.model.SocialUser;

public interface OAuthClientPort {
    SocialUser fetchUserInfo(String accessToken);
}
