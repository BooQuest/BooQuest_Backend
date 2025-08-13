package com.booquest.booquest_api.application.port.out.auth;

public interface JwtTokenPort {
    String generateToken(String provider, String providerId);
}
