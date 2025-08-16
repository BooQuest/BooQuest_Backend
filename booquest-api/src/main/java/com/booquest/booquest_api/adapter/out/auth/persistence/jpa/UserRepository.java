package com.booquest.booquest_api.adapter.out.auth.persistence.jpa;

import com.booquest.booquest_api.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u.id from User u where u.providerUserId = :providerUserId")
    Optional<Long> findUserIdByProviderUserId(@Param("providerUserId") String providerUserId);

    Optional<User> findByProviderAndProviderUserId(String provider, String providerUserId);
}
