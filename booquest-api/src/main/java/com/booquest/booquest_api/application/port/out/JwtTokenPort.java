package com.booquest.booquest_api.application.port.out;

public interface JwtTokenPort {
    String generateToken(String provider, String providerId);
}
