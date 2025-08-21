package com.booquest.booquest_api.adapter.in.auth.web;

import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginRequest;
import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingProgressInfo;
import com.booquest.booquest_api.application.port.in.auth.SocialLoginUseCase;
import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenRefreshResponse;
import com.booquest.booquest_api.application.port.in.onboarding.CheckSideJobStatusUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final SocialLoginUseCase socialLoginUseCase;
    private final TokenUseCase tokenUseCase;
    private final CheckSideJobStatusUseCase checkSideJobStatusUseCase;

    @PostMapping("/login")
    public ApiResponse<SocialLoginResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        SocialLoginResponse response = socialLoginUseCase.login(request.getAccessToken(), request.getProvider());

        SocialLoginResponse loginResponse = ProvideOnboardingStatus(response);

        return ApiResponse.success("로그인에 성공했습니다.", loginResponse);
    }

    private SocialLoginResponse ProvideOnboardingStatus(SocialLoginResponse response) {
        Long userId = response.getUserInfo().getUserId();
        OnboardingProgressInfo onboardingProgressInfo = checkSideJobStatusUseCase.getOnboardingProgress(userId);

        return SocialLoginResponse.of(response.getTokenInfo(), response.getUserInfo(), onboardingProgressInfo);
    }

    @PostMapping("/token/refresh")
    public ApiResponse<TokenRefreshResponse> refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        TokenRefreshResponse response = tokenUseCase.refreshAccessToken(refreshToken);
        return ApiResponse.success("토큰이 갱신되었습니다.", response);
    }
}
