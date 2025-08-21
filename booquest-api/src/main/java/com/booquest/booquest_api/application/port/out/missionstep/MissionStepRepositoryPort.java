package com.booquest.booquest_api.application.port.out.missionstep;

import com.booquest.booquest_api.domain.missionstep.model.MissionStep;

import java.util.List;
import java.util.Optional;

public interface MissionStepRepositoryPort {
    List<MissionStep> saveAll(Iterable<MissionStep> missions);

    MissionStep save(MissionStep missionStep);

    Optional<MissionStep> findById(Long id);
    
    List<MissionStep> findByMissionIdOrderBySeq(Long missionId);
}
