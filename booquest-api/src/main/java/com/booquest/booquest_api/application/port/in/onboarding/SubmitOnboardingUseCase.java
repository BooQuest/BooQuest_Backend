package com.booquest.booquest_api.application.port.in.onboarding;

import java.util.List;

public interface SubmitOnboardingUseCase {

    void submit(String providerUserId, String job, List<String> hobbies);
}
