package com.booquest.booquest_api.application.service.mission;

import com.booquest.booquest_api.application.port.in.mission.StartMissionUseCase;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.booquest.booquest_api.domain.mission.model.Mission;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeMissionStatusService implements StartMissionUseCase {

    private final MissionRepositoryPort missionRepository;

    @Transactional
    @Override
    public void start(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 미션을 찾을 수 없습니다."));

        mission.startWithSteps();
    }
}
