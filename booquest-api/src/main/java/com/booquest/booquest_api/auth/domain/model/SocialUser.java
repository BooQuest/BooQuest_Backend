package com.booquest.booquest_api.auth.domain.model;

public class SocialUser {
    private final String email;
    private final String nickname;
    private final String provider; // e.g., KAKAO, GOOGLE

    public SocialUser(String email, String nickname, String provider) {
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
    }
}
