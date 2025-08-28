package com.booquest.booquest_api.application.port.in.auth;

import com.booquest.booquest_api.adapter.in.auth.web.dto.LogoutResponse;

public interface LogoutUseCase {
    LogoutResponse logoutCurrentSession(Long userId, String rawRefreshToken);   // 현재 세션만 로그아웃(리프레시 토큰 필요)
    LogoutResponse logoutAllDevices(Long userId);   // 모든 기기에서 로그아웃
}
