package com.booquest.booquest_api.adapter.in.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionListResponse;
import com.booquest.booquest_api.adapter.in.mission.dto.MissionDetailResponseDto;
import com.booquest.booquest_api.adapter.in.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.adapter.in.mission.dto.MissionProgressResponseDto;
import com.booquest.booquest_api.adapter.in.mission.dto.MissionResponseDto;
import com.booquest.booquest_api.adapter.in.mission.dto.MissionCompleteResponse;
import com.booquest.booquest_api.application.port.in.mission.GetMissionListUseCase;
import com.booquest.booquest_api.adapter.in.mission.dto.MissionResponse;
import com.booquest.booquest_api.application.port.in.mission.GetMissionProgressUseCase;
import com.booquest.booquest_api.application.port.in.mission.SelectMissionUseCase;
import com.booquest.booquest_api.application.port.in.mission.StartMissionUseCase;
import com.booquest.booquest_api.application.port.in.mission.CompleteMissionUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.common.util.JsonMapperUtils;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
@Tag(name = "Mission", description = "메인퀘스트 API")
public class MissionController {

    private final @Qualifier("aiWebClient") WebClient webClient;

    private final GetMissionListUseCase getMissionListUseCase;
    private final SelectMissionUseCase selectMissionUseCase;
    private final GetMissionProgressUseCase getMissionProgressUseCase;
    private final StartMissionUseCase startMissionUseCase;
    private final CompleteMissionUseCase completeMissionUseCase;

    @PostMapping()
    @Operation(summary = "메인퀘스트 생성", description = "메인퀘스트를 생성합니다.")
    public ApiResponse<List<MissionResponseDto>> generate(@RequestBody MissionGenerateRequestDto requestDto) {
        String raw = webClient.post()                                   // POST로 호출해야 해서 필요
                .uri("/ai/generate-mission")                        // 호출할 AI 경로 지정 — 필요
                .contentType(MediaType.APPLICATION_JSON)                // 요청 바디가 JSON임을 명시 — 필요
                .bodyValue(requestDto)                                     // 보낼 페이로드 지정 — 필요
                .retrieve()                                             // 요청 실행 트리거 — 필요
                .bodyToMono(String.class)                               // 응답 바디를 “문자열”로 그대로 받음(파싱 없음) — 필요
                .block();

        List<MissionResponseDto> missions = JsonMapperUtils.parse(raw, new TypeReference<>() {});

        return ApiResponse.success("미션이 생성되었습니다.", missions);
    }

    @GetMapping
    @Operation(summary = "메인퀘스트 목록 조회", description = "로그인한 사용자의 메인퀘스트 목록을 조회합니다. </br>" +
            "sideJobId와 status 미 지정 시 (값이 null일 경우), 전체 목록을 조회합니다.")
    public ApiResponse<MissionListResponse> getMissionList(@RequestParam(required = false) Long sideJobId, @RequestParam(required = false) MissionStatus status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        List<MissionResponse> missionResponses = getMissionListUseCase.getMissionList(userId, sideJobId, status);

        MissionListResponse response = MissionListResponse.builder()
                .missions(missionResponses)
                .build();

        return ApiResponse.success("미션 목록이 조회되었습니다.", response);
    }

    @GetMapping("/{missionId}")
    @Operation(summary = "메인퀘스트 조회", description = "메인퀘스트 상세 정보를 조회합니다.")
    public ApiResponse<MissionDetailResponseDto> select(
            @PathVariable Long missionId
    ) {
        var mission = selectMissionUseCase.selectMission(missionId);
        var response = MissionDetailResponseDto.fromEntity(mission);

        return ApiResponse.success("미션을 조회합니다.", response);
    }

    @GetMapping("/progress")
    @Operation(summary = "퀘스트 진행률 조회", description = "로그인한 사용자의 퀘스트 진행률을 조회합니다.")
    public ApiResponse<MissionProgressResponseDto> getProgress(@RequestParam Long sideJobId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        var response = getMissionProgressUseCase.getMissionProgress(userId, sideJobId);

        return ApiResponse.success("퀘스트 진행률을 조회합니다.", response);
    }

    @PostMapping("/{missionId}/start")
    @Operation(summary = "미션을 시작해서 상태를 진행중으로 변경", description = "선택한 미션과 첫번째 부퀘스트를 진행중 상태로 변경합니다")
    public ApiResponse<Void> startMission(@PathVariable Long missionId) {
        startMissionUseCase.start(missionId);
        return ApiResponse.success("미션이 시작되었습니다");
    }

    @PostMapping("/{missionId}/complete")
    @Operation(summary = "메인퀘스트 완료", description = "모든 부퀘스트가 완료된 메인퀘스트를 완료 처리합니다. </br>" +
            "완료 시 부퀘스트 경험치, 광고/인증 보너스 경험치, 메인퀘스트 완료 경험치가 합산되어 캐릭터에 적용됩니다.")
    public ApiResponse<MissionCompleteResponse> completeMission(@PathVariable Long missionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        MissionCompleteResponse response = completeMissionUseCase.completeMission(missionId, userId);
        return ApiResponse.success("메인퀘스트가 완료되었습니다.", response);
    }
}
