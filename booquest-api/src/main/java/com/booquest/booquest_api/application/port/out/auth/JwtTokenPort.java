package com.booquest.booquest_api.application.port.out.auth;

import com.booquest.booquest_api.application.port.out.auth.dto.TokenInfo;

public interface JwtTokenPort {
    TokenInfo generateToken(Long userId, String email);
    boolean validateToken(String token);
    Long getUserIdFromToken(String token);
}
