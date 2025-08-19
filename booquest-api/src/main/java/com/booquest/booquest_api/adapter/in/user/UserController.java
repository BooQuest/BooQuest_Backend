package com.booquest.booquest_api.adapter.in.user;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingProgressInfo;
import com.booquest.booquest_api.adapter.out.user.dto.UserResponse;
import com.booquest.booquest_api.application.port.in.onboarding.CheckSideJobStatusUseCase;
import com.booquest.booquest_api.application.port.in.user.GetUserUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final GetUserUseCase getUserUseCase;
    private final CheckSideJobStatusUseCase checkSideJobStatusUseCase;

    @GetMapping("/me")
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
}
