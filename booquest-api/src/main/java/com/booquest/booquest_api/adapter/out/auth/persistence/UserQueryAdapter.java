package com.booquest.booquest_api.adapter.out.auth.persistence;

import com.booquest.booquest_api.application.port.out.user.UserCommandPort;
import com.booquest.booquest_api.application.port.out.user.UserQueryPort;
import com.booquest.booquest_api.adapter.out.auth.persistence.jpa.UserRepository;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import com.booquest.booquest_api.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements UserQueryPort, UserCommandPort {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId) {
        return userRepository.findByProviderAndProviderUserId(provider, providerUserId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public long deleteById(Long id) {
        return userRepository.deleteByIdReturningCount(id);
    }
}
