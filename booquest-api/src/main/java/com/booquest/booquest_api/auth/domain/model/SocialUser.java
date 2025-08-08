package com.booquest.booquest_api.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialUser {
    private final String email;
    private final String nickname;
    private final String provider; // e.g., KAKAO, GOOGLE
    private final String providerId;
}
