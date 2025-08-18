package com.booquest.booquest_api.adapter.in.auth.web;

import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginRequest;
import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.application.port.in.auth.SocialLoginUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final SocialLoginUseCase socialLoginUseCase;

    @PostMapping("/login")
    public ApiResponse<SocialLoginResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        SocialLoginResponse response = socialLoginUseCase.login(request.getAccessToken(), request.getProvider());
        return ApiResponse.success("로그인에 성공했습니다.", response);
    }
}
