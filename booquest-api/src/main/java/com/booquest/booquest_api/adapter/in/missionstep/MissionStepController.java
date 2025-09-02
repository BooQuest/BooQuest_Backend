package com.booquest.booquest_api.adapter.in.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepUpdateStatusRequest;
import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepUpdateStatusResponse;
import com.booquest.booquest_api.adapter.in.missionstep.dto.RegenerateMissionStepRequest;
import com.booquest.booquest_api.application.port.in.missionstep.DeleteMissionStepUseCase;
import com.booquest.booquest_api.application.port.in.missionstep.SelectMissionStepUseCase;
import com.booquest.booquest_api.application.port.in.missionstep.UpdateMissionStepStatusUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.common.util.JsonMapperUtils;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions/steps")
@Tag(name = "Mission Step", description = "부퀘스트 API")
public class MissionStepController {

    private final @Qualifier("aiWebClient") WebClient webClient;

    private final SelectMissionStepUseCase selectMissionStepUseCase;
    private final UpdateMissionStepStatusUseCase updateMissionStepStatusUseCase;
    private final DeleteMissionStepUseCase deleteMissionStepUseCase;

    @PostMapping()
    @Operation(summary = "부퀘스트 생성", description = "부퀘스트를 생성합니다.")
    public ApiResponse<List<MissionStepResponseDto>> generate(
            @RequestBody @Valid MissionStepGenerateRequestDto requestDto) {
        //해당 미션의 부퀘스트를 조회
        List<MissionStep> existedSteps = selectMissionStepUseCase.selectMissionStepsByMissionId(requestDto.missionId());

        if (!existedSteps.isEmpty()) {
            List<MissionStepResponseDto> missionSteps = existedSteps.stream()
                    .map(MissionStepResponseDto::fromEntity)
                    .toList();

            return ApiResponse.success("부퀘스트가 이미 존재합니다.", missionSteps);
        }

        String raw = webClient.post()                                   // POST로 호출해야 해서 필요
                .uri("/ai/generate-mission-step")                   // 호출할 AI 경로 지정 — 필요
                .contentType(MediaType.APPLICATION_JSON)                // 요청 바디가 JSON임을 명시 — 필요
                .bodyValue(requestDto)                                     // 보낼 페이로드 지정 — 필요
                .retrieve()                                             // 요청 실행 트리거 — 필요
                .bodyToMono(String.class)                               // 응답 바디를 “문자열”로 그대로 받음(파싱 없음) — 필요
                .block();

        List<MissionStepResponseDto> missionSteps = JsonMapperUtils.parse(raw, new TypeReference<>() {});

        return ApiResponse.success("부퀘스트가 생성되었습니다.", missionSteps);
    }

    @GetMapping("/{stepId}")
    @Operation(summary = "부퀘스트 조회", description = "부퀘스트 상세 정보를 조회합니다.")
    public ApiResponse<MissionStepResponseDto> getMissionStep(@PathVariable Long stepId) {
        var missionStep = selectMissionStepUseCase.selectMissionStep(stepId);
        var response = MissionStepResponseDto.fromEntity(missionStep);

        return ApiResponse.success("부퀘스트가 조회되었습니다.", response);
    }

    @PatchMapping("/{stepId}/status")
    @Operation(summary = "부퀘스트 상태 업데이트 (완료/완료취소 포함)", description = "로그인한 사용자의 부퀘스트 상태를 변경합니다. </br>상태 변경 시, 캐릭터 경험치도 업데이트됩니다.")
    public ApiResponse<MissionStepUpdateStatusResponse> updateStatus(@PathVariable Long stepId,
                                                                     @RequestBody @Valid MissionStepUpdateStatusRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        MissionStepUpdateStatusResponse response = updateMissionStepStatusUseCase.updateStatus(stepId, userId,
                request.getStatus());
        return ApiResponse.success("부퀘스트 상태가 변경되었습니다.", response);
    }

    @PostMapping("/regenerate")
    @Operation(summary = "부퀘스트 목록 재생성", description = "유저 피드백에 따라 부퀘스트 목록을 재생성합니다.")
    public ApiResponse<List<MissionStepResponseDto>> regenerateAll(@RequestBody @Valid RegenerateMissionStepRequest request) {

        //이전 부퀘스트 삭제
        deleteMissionStepUseCase.deleteAllByMissionId(request.generateMissionStep().missionId());
        //부퀘스트 생성 요청

        String raw = webClient.post()                                   // POST로 호출해야 해서 필요
                .uri("/ai/regenerate-mission-step")                           // 호출할 AI 경로 지정 — 필요
                .contentType(MediaType.APPLICATION_JSON)                       // 요청 바디가 JSON임을 명시 — 필요
                .bodyValue(request)                                             // 보낼 페이로드 지정 — 필요
                .retrieve()                                                    // 요청 실행 트리거 — 필요
                .bodyToMono(String.class)                                      // 응답 바디를 “문자열”로 그대로 받음(파싱 없음) — 필요
                .block();

        // JSON 문자열 → DTO 리스트로 변환
        List<MissionStepResponseDto> sideJobs = JsonMapperUtils.parse(raw, new TypeReference<>() {});

        return ApiResponse.success("부퀘스트가 재생성되었습니다.", sideJobs);
    }
}
