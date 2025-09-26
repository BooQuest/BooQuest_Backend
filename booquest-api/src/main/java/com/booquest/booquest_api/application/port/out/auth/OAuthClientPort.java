package com.booquest.booquest_api.application.port.out.auth;

import com.booquest.booquest_api.domain.user.model.SocialUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import java.text.ParseException;

public interface OAuthClientPort {
    SocialUser fetchUserInfo(String accessToken) throws JOSEException, ParseException, BadJOSEException;
}
