package com.booquest.booquest_api.adapter.in.auth.web;

import com.booquest.booquest_api.adapter.in.auth.web.dto.LogoutResponse;
import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginRequest;
import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingProgressInfo;
import com.booquest.booquest_api.application.port.in.auth.LogoutUseCase;
import com.booquest.booquest_api.application.port.in.auth.SocialLoginUseCase;
import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenRefreshResponse;
import com.booquest.booquest_api.application.port.in.onboarding.CheckSideJobStatusUseCase;
import com.booquest.booquest_api.application.port.out.character.CharacterQueryPort;
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
    private final LogoutUseCase logoutUseCase;
    private final CharacterQueryPort characterQueryPort;

    @PostMapping("/login")
    @Operation(summary = "소셜 로그인", description = "소셜 액세스 토큰을 검증하고 서비스 토큰을 발급합니다. 사용자 정보와 온보딩 진행 상태를 반환합니다.")
    public ApiResponse<SocialLoginResponse> socialLogin(@Valid @RequestBody SocialLoginRequest request) {
        SocialLoginResponse resp = socialLoginUseCase.login(request.getAccessToken(), request.getProvider());

        // 기존대로 온보딩 상태 합성
        SocialLoginResponse tmp = provideOnboardingStatus(resp);

        // 캐릭터 타입 조회
        var userId = tmp.getUserInfo().getUserId();
        var character = characterQueryPort.findByUserId(userId).orElse(null);

        // userInfo 재구성(기존 필드 + characterType)
        var enrichedUserInfo = SocialLoginResponse.UserInfo.builder()
                .userId(tmp.getUserInfo().getUserId())
                .email(tmp.getUserInfo().getEmail())
                .nickname(tmp.getUserInfo().getNickname())
                .profileImageUrl(tmp.getUserInfo().getProfileImageUrl())
                .characterType(character.getCharacterType())
                .build();

        var loginResponse = SocialLoginResponse.of(tmp.getTokenInfo(), enrichedUserInfo, tmp.getOnboardingProgressInfo());
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

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "제출된 리프레시 토큰을 무효화합니다. <br/>" +
            "리프레시 토큰은 X-Refresh-Token 헤더로 전달 가능합니다.")
    public ApiResponse<LogoutResponse> logoutCurrent(@RequestHeader(value = "X-Refresh-Token") String refreshToken) {
        LogoutResponse response = logoutUseCase.logoutCurrentSession(refreshToken);
        return ApiResponse.success("로그아웃되었습니다.", response);
    }

//    @PostMapping("/logout/all")
//    @Operation(summary = "모든 기기에서 로그아웃", description = "해당 계정의 모든 리프레시 토큰을 무효화합니다.")
//    public ApiResponse<LogoutResponse> logoutAll() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.parseLong(auth.getName());
//
//        LogoutResponse response = logoutUseCase.logoutAllDevices(userId);
//        return ApiResponse.success("모든 기기에서 로그아웃되었습니다.", response);
//    }
}
