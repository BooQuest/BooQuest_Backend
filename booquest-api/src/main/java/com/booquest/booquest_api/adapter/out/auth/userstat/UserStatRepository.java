package com.booquest.booquest_api.adapter.out.auth.userstat;

import com.booquest.booquest_api.domain.user.model.UserStat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatRepository extends JpaRepository<UserStat, Long> {
    long deleteByUserId(Long userId);
}
