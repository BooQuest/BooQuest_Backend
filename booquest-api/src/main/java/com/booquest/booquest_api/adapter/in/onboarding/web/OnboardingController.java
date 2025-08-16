package com.booquest.booquest_api.adapter.in.onboarding.web;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.dto.SubmitOnboardingData;
import com.booquest.booquest_api.application.port.in.onboarding.SubmitOnboardingUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
import com.booquest.booquest_api.application.port.in.user.UpdateUserProfileUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final SubmitOnboardingUseCase submitOnboardingUseCase;
    private final GenerateSideJobUseCase generateSideJobUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @PostMapping
    public ApiResponse<List<SideJobResponseDto>> generateSideJobFromOnboarding(
            @Valid @RequestBody OnboardingDataRequest request) {

        saveOnboardingProfile(request);
        updateUserProfileUseCase.updateNickname(request.userId(), request.nickname());

        List<SideJob> sideJobs = generateSideJobToAi(request);

        List<SideJobResponseDto> response = sideJobs.stream()
                .map(SideJobResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("부업이 생성되었습니다.", response);
    }

    private List<SideJob> generateSideJobToAi(OnboardingDataRequest request) {
        GenerateSideJobRequest sideJobData = new GenerateSideJobRequest(request.userId(), request.job(),
                request.hobbies(), request.expressionStyle(), request.strengthType(), request.desiredSideJob());
        //ai에게 부업 생성 요청
        return generateSideJobUseCase.generateSideJob(sideJobData);
    }

    private void saveOnboardingProfile(OnboardingDataRequest request) {
        SubmitOnboardingData onboardingData = new SubmitOnboardingData(request.userId(), request.job(),
                request.hobbies(), request.expressionStyle(), request.strengthType());
        //온보딩 데이터 DB 저장
        submitOnboardingUseCase.submit(onboardingData);
    }
}
