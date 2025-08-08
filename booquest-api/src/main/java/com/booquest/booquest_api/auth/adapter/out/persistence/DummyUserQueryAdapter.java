package com.booquest.booquest_api.auth.adapter.out.persistence;


import com.booquest.booquest_api.auth.application.port.out.UserQueryPort;
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
