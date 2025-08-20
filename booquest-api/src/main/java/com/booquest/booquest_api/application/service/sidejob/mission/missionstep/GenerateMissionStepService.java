package com.booquest.booquest_api.application.service.sidejob.mission.missionstep;

import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.adapter.in.onboarding.web.mission.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GenerateMissionResult;
import com.booquest.booquest_api.application.port.in.sidejob.mission.missionstep.GenerateMissionStepUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.mission.missionstep.GenerateMissionStepPort;
import com.booquest.booquest_api.application.port.out.sidejob.mission.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.mission.model.MissionStep;
import com.booquest.booquest_api.domain.sidejob.enums.MissionStatus;
import com.booquest.booquest_api.domain.sidejob.enums.StepStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateMissionStepService implements GenerateMissionStepUseCase {
     private final MissionStepRepositoryPort missionStepRepository;
    private final GenerateMissionStepPort missionGenerator;

    @Transactional
    @Override
    public List<MissionStep> generateMissionStep(MissionStepGenerateRequestDto generateDto) {
        ObjectMapper mapper = new ObjectMapper();
        var missionId = generateDto.missionId();

        //  ai 서버로 미션생성 요청
        var result = missionGenerator.generateMissionStep(generateDto);

        var missionSteps = result.steps().stream()
                .map(t -> {
                    return MissionStep.builder()
                            .missionId(missionId)
                            .title(t.title())
                            .status(StepStatus.PLANNED)
                            .seq(t.seq())
                            .detail(t.detail())
                            .build();
                }).toList();

         return missionStepRepository.saveAll(missionSteps);
    }
}
