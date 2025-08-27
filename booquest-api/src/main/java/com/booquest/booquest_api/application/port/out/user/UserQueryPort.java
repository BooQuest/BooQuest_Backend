package com.booquest.booquest_api.application.port.out.user;

import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import com.booquest.booquest_api.domain.user.model.User;

import java.util.Optional;

public interface UserQueryPort {
    Optional<User> findByProviderAndProviderUserId(AuthProvider provider, String providerId);

    Optional<User> findById(long id);
}
