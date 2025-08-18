package com.booquest.booquest_api.application.port.in.user;

import com.booquest.booquest_api.adapter.out.user.dto.UserResponse;
import org.springframework.security.core.Authentication;

public interface GetUserUseCase {
    UserResponse getUserById(Long userId);
    UserResponse getUserFromAuth(Authentication auth);
}
