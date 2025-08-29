package com.booquest.booquest_api.adapter.out.missionstep.persisitence;


import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    // Mission -> MissionStep 단방향(OneToMany)만 있어서 못 씀
//    @Query("""
//        select count(ms)
//        from MissionStep ms
//        where ms.status = :status
//          and exists (
//            select 1 from Mission m
//            where m.id = ms.missionId
//              and m.userId = :userId
//              and m.userSideJobId = :userSideJobId
//          )
//    """)
//    long countCompletedByUserAndSideJob(@Param("userId") Long userId,
//                                        @Param("userSideJobId") Long userSideJobId,
//                                        @Param("status") StepStatus status);

    @Query("""
        select count(ms)
        from MissionStep ms
        join Mission m on m.id = ms.mission.id
        where ms.status = :status
          and m.userId = :userId
          and m.sideJob.id = :userSideJobId
    """)
    long countCompletedByUserAndSideJob(@Param("userId") Long userId,
                                        @Param("userSideJobId") Long userSideJobId,
                                        @Param("status") StepStatus status);

    @Query("select s from MissionStep s where s.mission.id in :missionIds")
    List<MissionStep> findStepsByMissionIds(Collection<Long> missionIds);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from MissionStep s where s.mission.id in ( select m.id from Mission m where m.userId = :userId )")
    int deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM MissionStep ms WHERE ms.mission.id = :missionId")
    void deleteAllByMissionId(Long missionId);
}
