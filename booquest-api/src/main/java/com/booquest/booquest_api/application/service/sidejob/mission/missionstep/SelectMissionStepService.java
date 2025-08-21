package com.booquest.booquest_api.application.service.sidejob.mission.missionstep;

import com.booquest.booquest_api.application.port.in.mission.missionstep.SelectMissionStepUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.mission.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.MissionStep;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelectMissionStepService implements SelectMissionStepUseCase {
    private final MissionStepRepositoryPort missionStepRepository;

    @Override
    public MissionStep selectMissionStep(Long stepId) {
        return missionStepRepository.findById(stepId)
                .orElseThrow(() -> new EntityNotFoundException("Mission Step not found: " + stepId));
    }
}
