package com.booquest.booquest_api.application.port.out.mission;

import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;

import java.util.List;
import java.util.Optional;

public interface MissionRepositoryPort {
    Mission save(Mission mission);
    
    List<Mission> saveAll(Iterable<Mission> missions);

    Optional<Mission> findByIdWithSteps(Long missionId);

    boolean isExistByUserId(Long userId);

    boolean existsByUserIdAndStatusNot(Long userId, MissionStatus missionStatus);

    List<Mission> findByUserIdAndSideJobIdOrderByOrderNo(Long userId, Long sideJobId);

    Optional<Mission> findById(Long missionId);

    List<Mission> findByUserId(Long userId);

    List<Mission> findByUserIdAndStatus(Long userId, MissionStatus status);

    List<Mission> findByUserIdAndSideJobId(long userId, long sideJobSelectedId);

    List<Mission> findListWithOptionalFilters(Long userId, Long sideJobId, MissionStatus status);

    long deleteByUserId(Long userId);

    List<Mission> findBySideJobId(Long sideJobId);
}
