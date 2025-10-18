package com.booquest.booquest_api.application.service.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponseDto;
import com.booquest.booquest_api.application.port.in.missionstep.CreateMissionStepUseCase;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.domain.mission.enums.MainMission;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMissionStepService implements CreateMissionStepUseCase {

    private final MissionStepRepositoryPort missionStepRepositoryPort;
    private final MissionRepositoryPort missionRepositoryPort;

    @Override
    public List<MissionStepResponseDto> createMissionSteps(Long missionId) {

        Mission mission = missionRepositoryPort.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메인퀘스트입니다."));

        List<MissionStep> missionSteps = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            MissionStep missionStep = MissionStep.builder()
                    .mission(mission)
                    .seq(i)
                    .title(MainMission.getMissionSteps(i, mission.getOrderNo()))
                    .status(StepStatus.PLANNED)
                    .build();

            missionSteps.add(missionStep);
        }

        missionStepRepositoryPort.saveAll(missionSteps);

        return missionSteps.stream()
                .map(MissionStepResponseDto::fromEntity)
                .toList();
    }
}
