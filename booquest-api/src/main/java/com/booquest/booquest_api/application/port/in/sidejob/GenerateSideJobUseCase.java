package com.booquest.booquest_api.application.port.in.sidejob;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;

public interface GenerateSideJobUseCase {

    List<SideJob> generateSideJob(OnboardingDataRequest request);
}
