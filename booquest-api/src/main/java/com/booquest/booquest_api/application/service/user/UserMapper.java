package com.booquest.booquest_api.application.service.user;

import com.booquest.booquest_api.adapter.out.user.dto.UserResponse;
import com.booquest.booquest_api.domain.user.model.User;

final class UserMapper {
    private UserMapper() {}
    static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .provider(user.getProvider())
                .providerUserId(user.getProviderUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .socialNickname(user.getSocialNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
