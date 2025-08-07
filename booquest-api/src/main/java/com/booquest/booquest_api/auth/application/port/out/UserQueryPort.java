package com.booquest.booquest_api.auth.application.port.out;

public interface UserQueryPort {
    boolean existsByProviderAndProviderId(String provider, String providerId);
}
