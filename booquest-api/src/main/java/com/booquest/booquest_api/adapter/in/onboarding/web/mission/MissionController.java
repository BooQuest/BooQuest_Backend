package com.booquest.booquest_api.adapter.in.onboarding.web.mission;

import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionListResponse;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionDetailResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionProgressResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionResponseDto;
import com.booquest.booquest_api.application.port.in.mission.GetMissionListUseCase;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionResponse;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GenerateMissionUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GetMissionProgressUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.mission.SelectMissionUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.enums.MissionStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
@Tag(name = "Mission", description = "메인퀘스트 API")
public class MissionController {

    private final GenerateMissionUseCase generateMissionUseCase;
    private final GetMissionListUseCase getMissionListUseCase;
    private final SelectMissionUseCase selectMissionUseCase;
    private final GetMissionProgressUseCase getMissionProgressUseCase;

    @PostMapping()
    @Operation(summary = "메인퀘스트 생성", description = "메인퀘스트를 생성합니다.")
    public ApiResponse<List<MissionResponseDto>> generate(
            @RequestBody MissionGenerateRequestDto requestDto
    ) {
        List<Mission> missions = generateMissionUseCase.generateMission(requestDto);

        List<MissionResponseDto> response = missions.stream()
                .map(MissionResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("미션이 생성되었습니다.", response);
    }

    @GetMapping
    @Operation(summary = "메인퀘스트 목록 조회", description = "로그인한 사용자의 메인퀘스트 목록을 조회합니다.")
    public ApiResponse<MissionListResponse> getMissionList(@RequestParam(required = false) MissionStatus status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        List<MissionResponse> missionResponses = getMissionListUseCase.getMissionList(userId, status);

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
}
