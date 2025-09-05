package com.booquest.booquest_api.application.service.mission;

import com.booquest.booquest_api.application.port.in.mission.SelectMissionUseCase;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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

    @Override
    public List<Mission> selectMissionBySideJobId(Long sideJobId) {
        return missionRepository.findBySideJobId(sideJobId);
    }

    @Override
    public int selectOrderNoByMissionId(Long missionId) {
        return missionRepository.findOrderNoById(missionId);
    }
}
