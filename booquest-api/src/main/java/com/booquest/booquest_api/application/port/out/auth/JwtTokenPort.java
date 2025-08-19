package com.booquest.booquest_api.application.port.out.auth;

import com.booquest.booquest_api.application.port.out.auth.dto.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface JwtTokenPort {
    TokenInfo generateToken(Long userId, String email);
    TokenInfo generateTestToken();
    Jws<Claims> parse(String token);
}
