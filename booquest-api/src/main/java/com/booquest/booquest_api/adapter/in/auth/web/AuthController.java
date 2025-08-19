package com.booquest.booquest_api.adapter.in.auth.web;

import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginRequest;
import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.application.port.in.auth.SocialLoginUseCase;
import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.application.port.out.auth.dto.TokenRefreshResponse;
import com.booquest.booquest_api.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final SocialLoginUseCase socialLoginUseCase;
    private final TokenUseCase tokenUseCase;

    @PostMapping("/login")
    public ApiResponse<SocialLoginResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        SocialLoginResponse response = socialLoginUseCase.login(request.getAccessToken(), request.getProvider());
        return ApiResponse.success("로그인에 성공했습니다.", response);
    }

    @PostMapping("/token/refresh")
    public ApiResponse<TokenRefreshResponse> refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        TokenRefreshResponse response = tokenUseCase.refreshAccessToken(refreshToken);
        return ApiResponse.success("토큰이 갱신되었습니다.", response);
    }
}
