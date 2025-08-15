package com.booquest.booquest_api.application.port.out.ai;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.application.dto.SideJobGenerationResult;
import java.util.List;

public interface AiClientSideJobPort {
    SideJobGenerationResult generateSideJob(OnboardingDataRequest request);
}
