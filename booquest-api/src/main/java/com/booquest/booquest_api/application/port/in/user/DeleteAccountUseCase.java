package com.booquest.booquest_api.application.port.in.user;

import com.booquest.booquest_api.adapter.in.user.web.dto.DeleteAccountResponse;
import jakarta.annotation.Nullable;

public interface DeleteAccountUseCase {
    DeleteAccountResponse deleteUserCompletely(Long userId, @Nullable String providerAccessToken);
}
