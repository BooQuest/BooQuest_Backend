package com.booquest.booquest_api.adapter.in.auth.web.dto;

import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {
    private String accessToken;
    private AuthProvider provider;
}
