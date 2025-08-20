package com.booquest.booquest_api.application.port.in.sidejob;

import com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto.RegenerateRequest;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;

public interface RegenerateSideJobUseCase {
    List<SideJob> regenerateAll(RegenerateRequest request);

    SideJob regenerate(Long sideJobId);
}
