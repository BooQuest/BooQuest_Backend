package com.booquest.booquest_api.application.port.out.sidejob.mission;

import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.enums.MissionStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MissionRepositoryPort {
    List<Mission> saveAll(Iterable<Mission> missions);

    Optional<Mission> findByIdWithSteps(Long missionId);

    boolean isExistByUserId(Long userId);

    boolean existsByUserIdAndStatusNot(Long userId, MissionStatus missionStatus);
}
