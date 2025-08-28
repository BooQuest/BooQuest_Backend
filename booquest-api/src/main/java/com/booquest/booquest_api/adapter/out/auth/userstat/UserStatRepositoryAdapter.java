package com.booquest.booquest_api.adapter.out.auth.userstat;

import com.booquest.booquest_api.application.port.out.userstat.UserStatRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStatRepositoryAdapter implements UserStatRepositoryPort {
    private final UserStatRepository userStatRepository;

    @Override
    public long deleteByUserId(Long userId) {
        return userStatRepository.deleteByUserId(userId);
    }
}
