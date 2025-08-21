package com.booquest.booquest_api.adapter.in.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.application.port.in.mission.missionstep.GenerateMissionStepUseCase;
import com.booquest.booquest_api.application.port.in.mission.missionstep.SelectMissionStepUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.mission.model.MissionStep;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions/steps")
@Tag(name = "Mission Step", description = "부퀘스트 API")
public class MissionStepController {

    private final GenerateMissionStepUseCase generateMissionStepUseCase;
    private final SelectMissionStepUseCase selectMissionStepUseCase;

    @PostMapping()
    @Operation(summary = "부퀘스트 생성", description = "부퀘스트를 생성합니다.")
    public ApiResponse<List<MissionStepResponseDto>> generate(@RequestBody @Valid MissionStepGenerateRequestDto requestDto) {
        List<MissionStep> missionSteps = generateMissionStepUseCase.generateMissionStep(requestDto);

        List<MissionStepResponseDto> response = missionSteps.stream()
                .map(MissionStepResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("부퀘스트가 생성되었습니다.", response);
    }

    @GetMapping("/{stepId}")
    @Operation(summary = "부퀘스트 조회", description = "부퀘스트 상세 정보를 조회합니다.")
    public ApiResponse<MissionStepResponseDto> getMissionStep(@PathVariable Long stepId) {
        var missionStep = selectMissionStepUseCase.selectMissionStep(stepId);
        var response = MissionStepResponseDto.fromEntity(missionStep);

        return ApiResponse.success("부퀘스트가 조회되었습니다.", response);
    }
}
