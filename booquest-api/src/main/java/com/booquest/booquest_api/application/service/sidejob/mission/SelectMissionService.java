package com.booquest.booquest_api.application.service.sidejob.mission;

import com.booquest.booquest_api.application.port.in.sidejob.mission.SelectMissionUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SelectMissionService implements SelectMissionUseCase {
    private final MissionRepositoryPort missionRepository;

    @Override
    public Mission selectMission(Long missionId) {
        return missionRepository.findByIdWithSteps(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found: " + missionId));
    }
}
