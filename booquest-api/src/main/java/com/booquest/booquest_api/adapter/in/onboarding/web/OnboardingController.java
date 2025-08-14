package com.booquest.booquest_api.adapter.in.onboarding.web;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.application.port.in.onboarding.SubmitOnboardingUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> generateSideJobFromOnboarding(
            @Valid @RequestBody OnboardingDataRequest request) {

        submitOnboardingUseCase.submit(request.userId(), request.job(), request.hobbies());
        generateSideJobUseCase.generateSideJob(request.userId(), request.job(), request.hobbies());
        return null;
    }
}
