package com.booquest.booquest_api.application.port.out.user;

import com.booquest.booquest_api.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<Long> findUserIdByProviderUserId(String providerUserId);
}
