package com.booquest.booquest_api.application.service.user;

import com.booquest.booquest_api.adapter.in.user.web.dto.UserResponse;
import com.booquest.booquest_api.application.port.in.user.GetUserUseCase;
import com.booquest.booquest_api.application.port.in.user.UpdateUserProfileUseCase;
import com.booquest.booquest_api.application.port.out.user.UserCommandPort;
import com.booquest.booquest_api.application.port.out.user.UserQueryPort;
import com.booquest.booquest_api.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements GetUserUseCase, UpdateUserProfileUseCase {

    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userQueryPort.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserResponse.toResponse(user);

    }

    @Override
    public UserResponse getUserFromAuth(Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        return getUserById(userId);
    }

    @Override
    @Transactional
    public void updateNickname(long userId, String nickname) {
        // 예시 로직
        User user = userQueryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.updateNickname(nickname);
        userCommandPort.update(user);
    }
}
