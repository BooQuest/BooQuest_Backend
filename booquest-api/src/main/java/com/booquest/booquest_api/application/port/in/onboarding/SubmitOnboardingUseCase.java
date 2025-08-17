package com.booquest.booquest_api.application.port.in.onboarding;

import com.booquest.booquest_api.application.port.in.dto.SubmitOnboardingData;

public interface SubmitOnboardingUseCase {

    void submit(SubmitOnboardingData onboardingData);
}
