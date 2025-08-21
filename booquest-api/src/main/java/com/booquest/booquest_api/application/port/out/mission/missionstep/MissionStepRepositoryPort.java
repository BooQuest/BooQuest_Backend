package com.booquest.booquest_api.application.port.out.mission.missionstep;

import com.booquest.booquest_api.domain.mission.model.MissionStep;

import java.util.List;
import java.util.Optional;

public interface MissionStepRepositoryPort {
    List<MissionStep> saveAll(Iterable<MissionStep> missions);

    Optional<MissionStep> findById(Long id);
    
    List<MissionStep> findByMissionIdOrderBySeq(Long missionId);
}
