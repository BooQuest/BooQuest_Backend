package com.booquest.booquest_api.adapter.out.mission.persisitence;


import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE SideJob s SET s.isSelected = true WHERE s.id = :id")
    int updateSelectedTrue(@Param("id") Long id);

    @Query("SELECT DISTINCT m FROM Mission m LEFT JOIN FETCH m.steps WHERE m.id = :missionId")
    Optional<Mission> findByIdWithSteps(@Param("missionId") Long missionId);

    boolean existsByUserId(Long userId);

    boolean existsByUserIdAndStatusNot(Long userId, MissionStatus missionStatus);

    List<Mission> findByUserIdAndSideJobIdOrderByOrderNo(Long userId, Long sideJobId);

    @Query("SELECT DISTINCT m FROM Mission m LEFT JOIN FETCH m.steps s WHERE m.userId = :userId ORDER BY m.sideJob.id ASC, m.orderNo ASC, s.seq ASC, m.id ASC")
    List<Mission> findByUserIdWithSteps(@Param("userId") Long userId);

    @Query("SELECT DISTINCT m FROM Mission m LEFT JOIN FETCH m.steps s WHERE m.userId = :userId AND m.status = :status ORDER BY m.sideJob.id ASC, m.orderNo ASC, s.seq ASC, m.id ASC")
    List<Mission> findByUserIdAndStatusWithSteps(@Param("userId") Long userId, @Param("status") MissionStatus status);

    @Query("""
    select distinct m
    from Mission m
    left join fetch m.steps s
    where m.userId = :userId
        and (:sideJobId is null or m.sideJob.id = :sideJobId)
        and (m.status = :status or :status is null)
    order by m.sideJob.id asc, m.orderNo asc, s.seq asc, m.id asc
    """)
    List<Mission> findListWithOptionalFilters(
            @Param("userId") Long userId,
            @Param("sideJobId") Long sideJobId,
            @Param("status") MissionStatus status
    );

    List<Mission> findByUserIdAndSideJobId(long userId, long sideJobId);

    @Query("select m from Mission m where m.sideJob.id = :sid order by m.orderNo asc")
    List<Mission> findMissionsBySideJobId(Long sid);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Mission m where m.userId = :userId")
    int deleteByUserId(@Param("userId") Long userId);
}
