package com.booquest.booquest_api.application.service.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.missionstep.GenerateMissionStepUseCase;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.missionstep.GenerateMissionStepPort;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateMissionStepService implements GenerateMissionStepUseCase {
    private final MissionStepRepositoryPort missionStepRepository;
    private final GenerateMissionStepPort missionGenerator;
    private final MissionRepositoryPort missionRepositoryPort;

    @Transactional
    @Override
    public List<MissionStep> generateMissionStep(MissionStepGenerateRequestDto generateDto) {
        var missionId = generateDto.missionId();
        Mission mission = missionRepositoryPort.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("미션이 존재하지 않습니다"));

        //  ai 서버로 미션생성 요청
        var result = missionGenerator.generateMissionStep(generateDto);

        // 기존 스텝 조회
        List<MissionStep> existingSteps = missionStepRepository.findByMissionId(missionId);
        Map<Integer, MissionStep> existingMap = existingSteps.stream()
                .collect(Collectors.toMap(MissionStep::getSeq, step -> step));

        // upsert 수행
        List<MissionStep> missionSteps = result.steps().stream()
                .map(t -> {
                    MissionStep existing = existingMap.get(t.seq());
                    if (existing != null) {
                        existing.updateTitleAndDetail(t.title(), t.detail());
                        return existing;
                    } else {
                        return MissionStep.builder()
                                .mission(mission)
                                .title(t.title())
                                .status(StepStatus.PLANNED)
                                .seq(t.seq())
                                .detail(t.detail())
                                .build();
                    }
                })
                .toList();

         return missionStepRepository.saveAll(missionSteps);
    }
}
