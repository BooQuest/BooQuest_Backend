package com.booquest.booquest_api.domain.user.model;

import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SocialUser {
    private final String email;
    private final String nickname;
    private final AuthProvider provider; // e.g., KAKAO, GOOGLE
    private final String providerId;
    private final String profileImageUrl;
}
