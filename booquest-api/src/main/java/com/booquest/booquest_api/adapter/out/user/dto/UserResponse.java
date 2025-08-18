package com.booquest.booquest_api.adapter.out.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String provider;
    private String providerUserId;
    private String email;
    private String nickname;
    private String socialNickname;
    private String profileImageUrl;
}
