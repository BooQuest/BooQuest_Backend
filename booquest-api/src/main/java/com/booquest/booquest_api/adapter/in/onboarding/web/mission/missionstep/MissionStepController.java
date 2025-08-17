package com.booquest.booquest_api.adapter.in.onboarding.web.mission.missionstep;

import com.booquest.booquest_api.adapter.in.onboarding.web.mission.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.application.port.in.sidejob.mission.missionstep.GenerateMissionStepUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.mission.missionstep.SelectMissionStepUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.mission.model.MissionStep;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions/steps")
public class MissionStepController {

    private final GenerateMissionStepUseCase generateMissionStepUseCase;
    private final SelectMissionStepUseCase selectMissionStepUseCase;

    @PostMapping()
    public ApiResponse<List<MissionStepResponseDto>> generate(@RequestBody @Valid MissionStepGenerateRequestDto requestDto) {
        List<MissionStep> missionSteps = generateMissionStepUseCase.generateMissionStep(requestDto);

        List<MissionStepResponseDto> response = missionSteps.stream()
                .map(MissionStepResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("부퀘스트가 생성되었습니다.", response);
    }

    @GetMapping("/{stepId}")
    public ApiResponse<MissionStepResponseDto> list(@PathVariable Long stepId) {
        var missionStep = selectMissionStepUseCase.selectMissionStep(stepId);
        var response = MissionStepResponseDto.fromEntity(missionStep);

        return ApiResponse.success("부퀘스트가 조회되었습니다.", response);
    }
}
