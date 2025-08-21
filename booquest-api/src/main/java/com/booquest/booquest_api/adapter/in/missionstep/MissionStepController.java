package com.booquest.booquest_api.adapter.in.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepUpdateStatusRequest;
import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepUpdateStatusResponse;
import com.booquest.booquest_api.application.port.in.missionstep.GenerateMissionStepUseCase;
import com.booquest.booquest_api.application.port.in.missionstep.SelectMissionStepUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.application.port.in.missionstep.UpdateMissionStepStatusUseCase;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
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
    private final UpdateMissionStepStatusUseCase updateMissionStepStatusUseCase;

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

//    @PostMapping("/{stepId}/complete")
//    @Operation(summary = "부퀘스트 완료", description = "부퀘스트를 완료 처리합니다. 완료 시 캐릭터에 10 EXP가 부여됩니다.")
//    public ApiResponse<MissionStepResponseDto> complete(@PathVariable Long stepId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.parseLong(auth.getName());
//
//        var updated = updateMissionStepStatusUseCase.updateStatus(stepId, userId, StepStatus.COMPLETED);
//        var response = MissionStepResponseDto.fromEntity(updated);
//        return ApiResponse.success("부퀘스트가 완료되었습니다.", response);
//    }

    @PatchMapping("/{stepId}/status")
    @Operation(summary = "부퀘스트 상태 변경", description = "부퀘스트 상태를 변경합니다(완료/완료취소 포함).")
    public ApiResponse<MissionStepUpdateStatusResponse> updateStatus(@PathVariable Long stepId, @RequestBody @Valid MissionStepUpdateStatusRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        MissionStepUpdateStatusResponse response = updateMissionStepStatusUseCase.updateStatus(stepId, userId, request.getStatus());
        return ApiResponse.success("부퀘스트 상태가 변경되었습니다.", response);
    }
}
