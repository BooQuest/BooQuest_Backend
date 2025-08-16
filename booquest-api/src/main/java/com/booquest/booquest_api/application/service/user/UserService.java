package com.booquest.booquest_api.application.service.user;

import com.booquest.booquest_api.application.port.in.user.UpdateUserProfileUseCase;
import com.booquest.booquest_api.application.port.out.user.UserQueryPort;
import com.booquest.booquest_api.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UpdateUserProfileUseCase {

    private final UserQueryPort userQueryPort;

    @Override
    public void updateNickname(long userId, String nickname) {
        // 예시 로직
        User user = userQueryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.updateNickname(nickname);
        userQueryPort.update(user);
    }
}
