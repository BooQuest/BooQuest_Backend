package com.booquest.booquest_api.auth.adapter.out.jwt;


import com.booquest.booquest_api.auth.application.port.out.JwtTokenPort;
import org.springframework.stereotype.Component;

@Component
public class DummyJwtTokenAdapter implements JwtTokenPort {

    @Override
    public String generateToken(String provider, String providerId) {
        return "fake-jwt-token-for-user-" + provider + providerId;
    }
}
