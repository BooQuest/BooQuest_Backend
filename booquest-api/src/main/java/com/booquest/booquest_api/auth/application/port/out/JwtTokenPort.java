package com.booquest.booquest_api.auth.application.port.out;

public interface JwtTokenPort {
    String generateToken(String provider, String providerId);
}
