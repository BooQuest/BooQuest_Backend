package com.booquest.booquest_api.adapter.in.user;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingProgressInfo;
import com.booquest.booquest_api.adapter.in.user.web.dto.DeleteAccountResponse;
import com.booquest.booquest_api.adapter.in.user.web.dto.MyActivitiesSummaryResponse;
import com.booquest.booquest_api.adapter.in.user.web.dto.UserResponse;
import com.booquest.booquest_api.application.port.in.onboarding.CheckSideJobStatusUseCase;
import com.booquest.booquest_api.application.port.in.user.DeleteAccountUseCase;
import com.booquest.booquest_api.application.port.in.user.GetMyActivitiesSummaryUseCase;
import com.booquest.booquest_api.application.port.in.user.GetUserUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User", description = "사용자 API")
public class UserController {

    private final GetUserUseCase getUserUseCase;
    private final CheckSideJobStatusUseCase checkSideJobStatusUseCase;
    private final GetMyActivitiesSummaryUseCase getMyActivitiesSummaryUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    @GetMapping("/me")
    @Operation(summary = "로그인한 사용자 조회", description = "로그인한 사용자의 상세 정보를 조회합니다.")
    public ApiResponse<UserResponse> getUserFromAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserResponse response = getUserUseCase.getUserFromAuth(auth);

        UserResponse userResponse = provideOnboardingStatus(response);
        return ApiResponse.success("사용자 정보가 조회되었습니다.", userResponse);
    }

    private UserResponse provideOnboardingStatus(UserResponse response) {
        Long userId = response.getId();
        OnboardingProgressInfo onboardingProgressInfo = checkSideJobStatusUseCase.getOnboardingProgress(userId);

        return UserResponse.of(response, onboardingProgressInfo);
    }

    @GetMapping("/me/activities/summary")
    @Operation(summary = "나의 활동 요약 조회", description = "로그인한 사용자의 활동 요약 정보를 조회합니다.")
    public ApiResponse<MyActivitiesSummaryResponse> getMyActivities() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        MyActivitiesSummaryResponse data = getMyActivitiesSummaryUseCase.getSummary(userId);
        return ApiResponse.success("나의 활동 요약이 조회되었습니다.", data);
    }

    @DeleteMapping("/me")
    @Operation(summary = "탈퇴 (사용자 계정 삭제)", description = "로그인한 사용자의 계정과 모든 관련 데이터를 삭제합니다. </br>" +
            "- 삭제 대상: user_side_jobs, side_jobs, tokens, income, proofs, ad_views, missions, mission_steps, onboarding_categories, onboarding_profiles, step_progress, user_characters, user_stats, users 테이블의 사용자 연계 데이터 전부 </br>" +
            "- 액세스 토큰은 만료까지 유효하므로, 프론트에서 즉시 토큰/로컬데이터를 삭제하세요.")
    public ApiResponse<DeleteAccountResponse> deleteUser(@RequestHeader(value = "X-Provider-Access-Token", required = false) String providerAccessToken) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        DeleteAccountResponse result = deleteAccountUseCase.deleteUserCompletely(userId, providerAccessToken);
        return ApiResponse.success("계정이 탈퇴되었습니다.", result);
    }
}
