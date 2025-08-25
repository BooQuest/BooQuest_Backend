package com.booquest.booquest_api.adapter.out.missionstep.persisitence;


import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionStepRepository extends JpaRepository<MissionStep, Long> {
    List<MissionStep> findByMissionIdOrderBySeq(Long missionId);

    List<MissionStep> findByMissionId(Long missionId);

    @Query(value = """
      select count(ms.id)
      from mission_steps ms
      join missions m on ms.mission_id = m.id
      where m.user_id = :userId and ms.status = 'COMPLETED'
    """, nativeQuery = true)
    long countCompletedStepsByUserId(@Param("userId") Long userId);
}
