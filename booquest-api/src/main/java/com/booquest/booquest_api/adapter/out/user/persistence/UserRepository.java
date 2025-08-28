package com.booquest.booquest_api.adapter.out.user.persistence;

import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import com.booquest.booquest_api.domain.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u.id from User u where u.provider = :provider and u.providerUserId = :providerUserId")
    Optional<Long> findIdByProviderAndProviderUserId(@Param("provider") AuthProvider provider,
                                                     @Param("providerUserId") String providerUserId);

    Optional<User> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from User u where u.id = :id")
    int deleteByIdReturningCount(@Param("id") Long id);
}
