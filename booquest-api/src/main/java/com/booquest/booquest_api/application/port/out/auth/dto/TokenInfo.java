package com.booquest.booquest_api.application.port.out.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
}