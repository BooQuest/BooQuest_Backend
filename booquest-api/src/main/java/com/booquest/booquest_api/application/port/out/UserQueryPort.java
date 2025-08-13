package com.booquest.booquest_api.application.port.out;

public interface UserQueryPort {
    boolean existsByProviderAndProviderId(String provider, String providerId);
}
