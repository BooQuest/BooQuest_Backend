package com.booquest.booquest_api.application.port.out.sidejob.mission;

import com.booquest.booquest_api.domain.mission.model.Mission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MissionRepositoryPort {
    List<Mission> saveAll(Iterable<Mission> missions);

    Optional<Mission> findByIdWithSteps(Long missionId);
}
