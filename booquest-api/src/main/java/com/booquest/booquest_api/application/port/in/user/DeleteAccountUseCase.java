package com.booquest.booquest_api.application.port.in.user;

import com.booquest.booquest_api.adapter.in.user.web.dto.DeleteAccountResponse;

public interface DeleteAccountUseCase {
    DeleteAccountResponse deleteUserCompletely(Long userId);
}
