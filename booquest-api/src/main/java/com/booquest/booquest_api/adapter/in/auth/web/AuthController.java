package com.booquest.booquest_api.adapter.in.auth.web;

import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginRequest;
import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingProgressInfo;
import com.booquest.booquest_api.application.port.in.auth.SocialLoginUseCase;
import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenRefreshResponse;
import com.booquest.booquest_api.application.port.in.onboarding.CheckSideJobStatusUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 및 토큰 API")
public class AuthController {

    private final SocialLoginUseCase socialLoginUseCase;
    private final TokenUseCase tokenUseCase;
    private final CheckSideJobStatusUseCase checkSideJobStatusUseCase;

    @PostMapping("/login")
    @Operation(summary = "소셜 로그인", description = "소셜 액세스 토큰을 검증하고 서비스 토큰을 발급합니다. 사용자 정보와 온보딩 진행 상태를 반환합니다.")
    public ApiResponse<SocialLoginResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        SocialLoginResponse response = socialLoginUseCase.login(request.getAccessToken(), request.getProvider());

        SocialLoginResponse loginResponse = provideOnboardingStatus(response);

        return ApiResponse.success("로그인에 성공했습니다.", loginResponse);
    }

    private SocialLoginResponse provideOnboardingStatus(SocialLoginResponse response) {
        Long userId = response.getUserInfo().getUserId();
        OnboardingProgressInfo onboardingProgressInfo = checkSideJobStatusUseCase.getOnboardingProgress(userId);

        return SocialLoginResponse.of(response.getTokenInfo(), response.getUserInfo(), onboardingProgressInfo);
    }

    @PostMapping("/token/refresh")
    @Operation(summary = "액세스 토큰 재발급", description = "헤더의 리프레시 토큰을 검증해 새 액세스 토큰을 발급합니다.")
    public ApiResponse<TokenRefreshResponse> refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        TokenRefreshResponse response = tokenUseCase.refreshAccessToken(refreshToken);
        return ApiResponse.success("토큰이 갱신되었습니다.", response);
    }
}
