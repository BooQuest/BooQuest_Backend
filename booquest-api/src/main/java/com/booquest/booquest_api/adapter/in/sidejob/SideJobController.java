package com.booquest.booquest_api.adapter.in.sidejob;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.adapter.in.sidejob.dto.RegenerateAllSideJobRequest;
import com.booquest.booquest_api.adapter.in.sidejob.dto.RegenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.sidejob.RegenerateSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.SelectSideJobUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sideJob")
@Tag(name = "Side Job Recommendation", description = "부업 추천 API")
public class SideJobController {

    private final RegenerateSideJobUseCase regenerateSideJobUseCase;
    private final SelectSideJobUseCase selectSideJobUseCase;

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

    @GetMapping("/{userId}")
    @Operation(summary = "유저의 추천받은 부업들 조회", description = "유저가 ai로 추천받은 부업 3개를 조회합니다.")
    public ApiResponse<List<SideJobResponseDto>> getRecommendedSideJobs(@PathVariable Long userId) {
        List<SideJob> sideJobs = selectSideJobUseCase.selectSideJobsByUserId(userId);

        List<SideJobResponseDto> response = new ArrayList<>();
        for (SideJob sideJob : sideJobs) {
            response.add(SideJobResponseDto.fromEntity(sideJob));
        }

        return ApiResponse.success("부업들을 조회하였습니다", response);
    }
}
