package com.booquest.booquest_api.adapter.in.sidejob.dto;

import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import jakarta.validation.Valid;
import java.util.List;

public record RegenerateAllSideJobRequest(List<Long> sideJobIds, @Valid GenerateSideJobRequest generateSideJobRequest) {
}
