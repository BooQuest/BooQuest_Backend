package com.booquest.booquest_api.adapter.in.onboarding.web.mission;

import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionDetailResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionProgressResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionResponseDto;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GenerateMissionUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GetMissionProgressUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.mission.SelectMissionUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.mission.model.Mission;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController {

    private final GenerateMissionUseCase generateMissionUseCase;
    private final SelectMissionUseCase selectMissionUseCase;
    private final GetMissionProgressUseCase getMissionProgressUseCase;

    @PostMapping()
    public ApiResponse<List<MissionResponseDto>> generate(
            @RequestBody MissionGenerateRequestDto requestDto
    ) {
        List<Mission> missions = generateMissionUseCase.generateMission(requestDto);

        List<MissionResponseDto> response = missions.stream()
                .map(MissionResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("미션이 생성되었습니다.", response);
    }

    @GetMapping("/{missionId}")
    public ApiResponse<MissionDetailResponseDto> select(
            @PathVariable Long missionId
    ) {
        var mission = selectMissionUseCase.selectMission(missionId);
        var response = MissionDetailResponseDto.fromEntity(mission);

        return ApiResponse.success("미션을 조회합니다.", response);
    }

    @GetMapping("/progress")
    public ApiResponse<MissionProgressResponseDto> getProgress(@RequestParam Long sideJobId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());
        var response = getMissionProgressUseCase.getMissionProgress(userId, sideJobId);

        return ApiResponse.success("퀘스트 진행률을 조회합니다.", response);
    }
}
