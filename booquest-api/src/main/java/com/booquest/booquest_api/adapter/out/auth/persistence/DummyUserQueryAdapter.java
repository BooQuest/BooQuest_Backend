package com.booquest.booquest_api.adapter.out.auth.persistence;


import com.booquest.booquest_api.application.port.out.auth.UserQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DummyUserQueryAdapter implements UserQueryPort {

    @Override
    public boolean existsByProviderAndProviderId(String provider, String providerId) {
        return providerId.equals("exist-user");
    }
}
