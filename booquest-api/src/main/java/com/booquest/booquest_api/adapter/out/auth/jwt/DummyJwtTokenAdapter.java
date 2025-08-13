package com.booquest.booquest_api.adapter.out.auth.jwt;


import com.booquest.booquest_api.application.port.out.auth.JwtTokenPort;
import org.springframework.stereotype.Component;

@Component
public class DummyJwtTokenAdapter implements JwtTokenPort {

    @Override
    public String generateToken(String provider, String providerId) {
        return "fake-jwt-token-for-user-" + provider + providerId;
    }
}
