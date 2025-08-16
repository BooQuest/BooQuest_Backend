package com.booquest.booquest_api.adapter.in.onboarding.web.mission;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionGenerateResponseDto;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GenerateMissionUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/missions")
public class MissionController {

    private final GenerateMissionUseCase generateMissionUseCase;

    @PostMapping("/generate")
    public ApiResponse<List<MissionGenerateResponseDto>> generate(
            @RequestBody MissionGenerateRequestDto requestDto
    ) {
        List<Mission> missions = generateMissionUseCase.generateMission(requestDto);

        List<MissionGenerateResponseDto> response = missions.stream()
                .map(MissionGenerateResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("미션이 생성되었습니다.", response);
    }
}
