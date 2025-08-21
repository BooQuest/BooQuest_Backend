package com.booquest.booquest_api.application.service.missionstep;

import com.booquest.booquest_api.application.port.in.missionstep.SelectMissionStepUseCase;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
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
