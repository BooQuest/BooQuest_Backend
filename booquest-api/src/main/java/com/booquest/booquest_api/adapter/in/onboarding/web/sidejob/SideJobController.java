package com.booquest.booquest_api.adapter.in.onboarding.web.sidejob;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto.RegenerateAllSideJobRequest;
import com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto.RegenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.sidejob.RegenerateSideJobUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Side Job", description = "부업 API")
public class SideJobController {

    public final RegenerateSideJobUseCase regenerateSideJobUseCase;

    @PostMapping("/regenerate")
    @Operation(summary = "부업 목록 재생성", description = "요청 기준에 따라 부업 목록을 재생성합니다.")
    public ApiResponse<List<SideJobResponseDto>> regenerateAll(@RequestBody @Valid RegenerateAllSideJobRequest request) {
        List<SideJob> sideJobs = regenerateSideJobUseCase.regenerateAll(request);

        List<SideJobResponseDto> response = sideJobs.stream()
                .map(SideJobResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("부업이 재생성되었습니다.", response);
    }

    @PostMapping("/regenerate/{sideJobId}")
    @Operation(summary = "부업 재생성", description = "부업 ID로 지정한 부업을 재생성합니다.")
    public ApiResponse<SideJobResponseDto> regenerateById(@PathVariable Long sideJobId,
                                                          @RequestBody @Valid RegenerateSideJobRequest request) {

        SideJob sideJob = regenerateSideJobUseCase.regenerate(sideJobId, request);

        SideJobResponseDto response = SideJobResponseDto.fromEntity(sideJob);

        return ApiResponse.success("부업이 재생성되었습니다.", response);
    }
}
