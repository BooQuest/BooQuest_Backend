package com.booquest.booquest_api.adapter.in.onboarding.web;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobDetailResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.application.port.in.character.CreateCharacterUseCase;
import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.dto.SubmitOnboardingData;
import com.booquest.booquest_api.application.port.in.onboarding.SubmitOnboardingUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.SelectSideJobUseCase;
import com.booquest.booquest_api.application.port.in.user.UpdateUserProfileUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.character.enums.CharacterType;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final SubmitOnboardingUseCase submitOnboardingUseCase;
    private final GenerateSideJobUseCase generateSideJobUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final CreateCharacterUseCase createCharacterUseCase;
    private final SelectSideJobUseCase selectSideJobUseCase;

    @PostMapping
    public ApiResponse<List<SideJobResponseDto>> generateSideJobFromOnboarding(
            @Valid @RequestBody OnboardingDataRequest request) {

        saveOnboardingProfile(request);
        updateUserProfileUseCase.updateNickname(request.userId(), request.nickname());

        CharacterType characterType = CharacterType.from(request.characterType());
        createCharacterUseCase.createCharacter(request.userId(), characterType);

        List<SideJob> sideJobs = generateSideJobToAi(request);

        List<SideJobResponseDto> response = sideJobs.stream()
                .map(SideJobResponseDto::fromEntity)
                .toList();

        return ApiResponse.success("부업이 생성되었습니다.", response);
    }

    @GetMapping("/{sideJobId}")
    public ApiResponse<SideJobDetailResponseDto> list(@PathVariable Long sideJobId) {
        var sideJobDetail = selectSideJobUseCase.selectSideJob(sideJobId);
        var response = SideJobDetailResponseDto.fromEntity(sideJobDetail);

        return ApiResponse.success("부업이 조회되었습니다.", response);
    }

    private List<SideJob> generateSideJobToAi(OnboardingDataRequest request) {
        GenerateSideJobRequest sideJobData = new GenerateSideJobRequest(request.userId(), request.job(),
                request.hobbies(), request.expressionStyle(), request.strengthType());
        //ai에게 부업 생성 요청
        return generateSideJobUseCase.generateSideJob(sideJobData);
    }

    private void saveOnboardingProfile(OnboardingDataRequest request) {
        SubmitOnboardingData onboardingData = new SubmitOnboardingData(request.userId(), request.job(),
                request.hobbies(), request.expressionStyle(), request.strengthType());
        //온보딩 데이터 DB 저장
        submitOnboardingUseCase.submit(onboardingData);
    }
}
