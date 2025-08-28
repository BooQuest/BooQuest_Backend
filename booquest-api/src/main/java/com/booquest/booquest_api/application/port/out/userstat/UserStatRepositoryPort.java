package com.booquest.booquest_api.application.port.out.userstat;

public interface UserStatRepositoryPort {
    long deleteByUserId(Long userId);
}
