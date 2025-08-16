package com.booquest.booquest_api.application.port.out.sidejob;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.application.port.in.sidejob.SideJobGenerationResult;

public interface GenerateSideJobPort {
    SideJobGenerationResult generateSideJob(OnboardingDataRequest request);
}
