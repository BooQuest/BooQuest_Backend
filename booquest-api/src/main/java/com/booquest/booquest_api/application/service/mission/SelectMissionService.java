package com.booquest.booquest_api.application.service.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionResponse;
import com.booquest.booquest_api.application.port.in.mission.GetMissionListUseCase;
import com.booquest.booquest_api.application.port.in.mission.SelectMissionUseCase;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.mission.policy.MissionExpPolicy;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SelectMissionService implements SelectMissionUseCase, GetMissionListUseCase {
    private final MissionRepositoryPort missionRepository;
    private final MissionExpPolicy missionExpPolicy;

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

    @Override
    public List<MissionResponse> getMissionList(Long userId, Long sideJobId, MissionStatus status) {
        List<Mission> missions = missionRepository.findListWithOptionalFilters(userId, sideJobId, status);

        return missions.stream()
                .map(mission -> MissionResponse.toResponse(mission, missionExpPolicy.totalExpFor(mission)))
                .collect(Collectors.toList());
    }
}
