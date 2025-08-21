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

    @Query("SELECT DISTINCT m FROM Mission m LEFT JOIN FETCH m.steps s WHERE m.userId = :userId ORDER BY m.orderNo, s.seq")
    List<Mission> findByUserIdWithSteps(@Param("userId") Long userId);

    @Query("SELECT m FROM Mission m LEFT JOIN FETCH m.steps WHERE m.userId = :userId AND m.status = :status ORDER BY m.orderNo")
    List<Mission> findByUserIdAndStatusWithSteps(@Param("userId") Long userId, @Param("status") MissionStatus status);
}
