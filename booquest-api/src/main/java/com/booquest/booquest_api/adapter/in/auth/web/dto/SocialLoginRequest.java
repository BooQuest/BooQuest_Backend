package com.booquest.booquest_api.adapter.in.auth.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {
    private String accessToken;
    private String provider;
}
