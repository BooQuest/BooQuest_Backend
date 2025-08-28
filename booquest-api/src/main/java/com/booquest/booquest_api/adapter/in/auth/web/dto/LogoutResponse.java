package com.booquest.booquest_api.adapter.in.auth.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutResponse {
    private final boolean success;
    private final String scope; // current or all
    private final int revokedCount; // 삭제된(무효화된) 리프레시 토큰 개수
}
