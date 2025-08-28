package com.booquest.booquest_api.adapter.in.onboarding.web;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobDetailResponseDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.dto.SideJobResponseDto;
import com.booquest.booquest_api.application.port.in.character.CreateCharacterUseCase;
import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.dto.SubmitOnboardingData;
import com.booquest.booquest_api.application.port.in.onboarding.SubmitOnboardingUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.DeleteSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.SelectSideJobUseCase;
import com.booquest.booquest_api.application.port.in.user.UpdateUserProfileUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.common.util.JsonMapperUtils;
import com.booquest.booquest_api.domain.character.enums.CharacterType;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
@Tag(name = "Onboarding", description = "온보딩 API")
public class OnboardingController {

    private final @Qualifier("aiWebClient") WebClient webClient;

    private final SubmitOnboardingUseCase submitOnboardingUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final CreateCharacterUseCase createCharacterUseCase;
    private final SelectSideJobUseCase selectSideJobUseCase;
    private final DeleteSideJobUseCase deleteSideJobUseCase;

    @PostMapping
    @Operation(summary = "온보딩 및 부업 생성", description = "온보딩 데이터를 저장하고 닉네임 및 캐릭터를 설정한 뒤 AI로 부업 후보를 생성합니다.")
    public ApiResponse<List<SideJobResponseDto>> generateSideJobFromOnboarding(
            @Valid @RequestBody OnboardingDataRequest request) {

        saveOnboardingProfile(request);
        updateUserProfileUseCase.updateNickname(request.userId(), request.nickname());

        CharacterType characterType = CharacterType.from(request.characterType());
        createCharacterUseCase.createCharacter(request.userId(), characterType);

        deleteSideJobUseCase.deleteAllSideJob(request.userId());

        String raw = webClient.post()                                   // POST로 호출해야 해서 필요
                .uri("/ai/generate-side-job")                           // 호출할 AI 경로 지정 — 필요
                .contentType(MediaType.APPLICATION_JSON)                       // 요청 바디가 JSON임을 명시 — 필요
                .bodyValue(request)                                             // 보낼 페이로드 지정 — 필요
                .retrieve()                                                    // 요청 실행 트리거 — 필요
                .bodyToMono(String.class)                                      // 응답 바디를 “문자열”로 그대로 받음(파싱 없음) — 필요
                .block();                                                      // MVC 흐름에서 동기로 결과 필요 — 필요


        // JSON 문자열 → DTO 리스트로 변환
        List<SideJobResponseDto> sideJobs = JsonMapperUtils.parse(raw, new TypeReference<>() {});

        return ApiResponse.success("부업이 생성되었습니다.", sideJobs);
    }

    @GetMapping("/{sideJobId}")
    @Operation(summary = "부업 조회", description = "부업 상세 정보를 조회합니다.")
    public ApiResponse<SideJobDetailResponseDto> list(@PathVariable Long sideJobId) {
        var sideJobDetail = selectSideJobUseCase.selectSideJob(sideJobId);
        var response = SideJobDetailResponseDto.fromEntity(sideJobDetail);

        return ApiResponse.success("부업이 조회되었습니다.", response);
    }


    private void saveOnboardingProfile(OnboardingDataRequest request) {
        SubmitOnboardingData onboardingData = new SubmitOnboardingData(request.userId(), request.job(),
                request.hobbies(), request.expressionStyle(), request.strengthType());
        //온보딩 데이터 DB 저장
        submitOnboardingUseCase.submit(onboardingData);
    }
}
