package com.booquest.booquest_api.application.port.in.sidejob;

import com.booquest.booquest_api.adapter.in.sidejob.dto.RegenerateAllSideJobRequest;
import com.booquest.booquest_api.adapter.in.sidejob.dto.RegenerateSideJobRequest;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;

public interface RegenerateSideJobUseCase {
    List<SideJob> regenerateAll(RegenerateAllSideJobRequest request);

    SideJob regenerate(Long sideJobId, RegenerateSideJobRequest request);
}
