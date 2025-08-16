package com.booquest.booquest_api.application.port.out.sidejob;

import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.sidejob.SideJobGenerationResult;

public interface GenerateSideJobPort {
    SideJobGenerationResult generateSideJob(GenerateSideJobRequest request);
}
