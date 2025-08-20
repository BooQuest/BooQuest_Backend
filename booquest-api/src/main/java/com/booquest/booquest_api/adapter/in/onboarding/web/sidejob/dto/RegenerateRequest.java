package com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto;

import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import jakarta.validation.Valid;
import java.util.List;

public record RegenerateRequest(List<Long> sideJobIds, @Valid GenerateSideJobRequest generateSideJobRequest) {
}
