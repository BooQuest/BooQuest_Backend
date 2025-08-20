package com.booquest.booquest_api.adapter.in.onboarding.web.sidejob;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto.RegenerateAllSideJobRequest;
import com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto.RegenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.sidejob.RegenerateSideJobUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sideJob")
public class SideJobController {

    public final RegenerateSideJobUseCase regenerateSideJobUseCase;

    @PostMapping("/regenerate")
    public ApiResponse<List<SideJobResponseDto>> regenerateAll(@RequestBody @Valid RegenerateAllSideJobRequest request) {
        List<SideJob> sideJobs = regenerateSideJobUseCase.regenerateAll(request);

        List<SideJobResponseDto> response = sideJobs.stream()
                .map(SideJobResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("부업이 재생성되었습니다.", response);
    }

    @PostMapping("/regenerate/{sideJobId}")
    public ApiResponse<SideJobResponseDto> regenerateById(@PathVariable Long sideJobId,
                                                          @RequestBody @Valid RegenerateSideJobRequest request) {

        SideJob sideJob = regenerateSideJobUseCase.regenerate(sideJobId, request);

        SideJobResponseDto response = SideJobResponseDto.fromEntity(sideJob);

        return ApiResponse.success("부업이 재생성되었습니다.", response);
    }
}
