package com.booquest.booquest_api.adapter.out.sidejob.mission.missionstep.persisitence;


import com.booquest.booquest_api.domain.mission.model.MissionStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionStepRepository extends JpaRepository<MissionStep, Long> {
    List<MissionStep> findByMissionIdOrderBySeq(Long missionId);
}
