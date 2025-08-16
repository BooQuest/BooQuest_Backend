package com.booquest.booquest_api.adapter.in.onboarding.web;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.application.port.in.onboarding.SubmitOnboardingUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
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

    @PostMapping
    public ApiResponse<List<SideJobResponseDto>> generateSideJobFromOnboarding(
            @Valid @RequestBody OnboardingDataRequest request) {

        //온보딩 데이터 DB 저장
        submitOnboardingUseCase.submit(request);

        //ai에게 부업 생성 요청
        List<SideJob> sideJobs = generateSideJobUseCase.generateSideJob(request);

        List<SideJobResponseDto> response = sideJobs.stream()
                .map(SideJobResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("부업이 생성되었습니다.", response);
    }
}
