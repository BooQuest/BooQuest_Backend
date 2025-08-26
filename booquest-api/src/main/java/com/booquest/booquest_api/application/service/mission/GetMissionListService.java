package com.booquest.booquest_api.application.service.mission;

import com.booquest.booquest_api.application.port.in.mission.GetMissionListUseCase;
import com.booquest.booquest_api.adapter.in.mission.dto.MissionResponse;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.booquest.booquest_api.domain.mission.policy.MissionExpPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMissionListService implements GetMissionListUseCase {

    private final MissionRepositoryPort missionRepositoryPort;
    private final MissionExpPolicy missionExpPolicy;

    @Override
    public List<MissionResponse> getMissionList(Long userId, Long sideJobId, MissionStatus status) {
        List<Mission> missions = missionRepositoryPort.findListWithOptionalFilters(userId, sideJobId, status);

        return missions.stream()
                .map(mission -> MissionResponse.toResponse(mission, missionExpPolicy.totalExpFor(mission)))
                .collect(Collectors.toList());
    }
}