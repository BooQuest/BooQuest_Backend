package com.booquest.booquest_api.application.port.out.user;

import com.booquest.booquest_api.domain.user.model.User;

public interface UserCommandPort {
    User save(User user);
    User update(User user);
    long deleteById(Long userId);
}
