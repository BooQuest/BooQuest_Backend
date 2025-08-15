package com.booquest.booquest_api.application.port.in.onboarding;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import java.util.List;

public interface SubmitOnboardingUseCase {

    void submit(OnboardingDataRequest request);
}
