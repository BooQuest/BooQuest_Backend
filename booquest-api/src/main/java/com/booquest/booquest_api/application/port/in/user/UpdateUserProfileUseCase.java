package com.booquest.booquest_api.application.port.in.user;

public interface UpdateUserProfileUseCase {
    void updateNickname(long userId, String nickname);
}
