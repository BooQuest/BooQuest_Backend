package com.booquest.booquest_api.adapter.in.onboarding.web;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.application.port.in.onboarding.SubmitOnboardingUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<SideJobResponseDto>> generateSideJobFromOnboarding(
            @Valid @RequestBody OnboardingDataRequest request) {

        submitOnboardingUseCase.submit(request.userId(), request.job(), request.hobbies());
        List<SideJob> sideJobs = generateSideJobUseCase.generateSideJob(request.userId(), request.job(), request.hobbies());

        List<SideJobResponseDto> response = sideJobs.stream()
                .map(SideJobResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }
}
