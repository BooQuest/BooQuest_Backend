package com.booquest.booquest_api.adapter.out.sidejob.persistence;


import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SideJobRepository extends JpaRepository<SideJob, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE SideJob s SET s.isSelected = true WHERE s.id = :id")
    int updateSelectedTrue(@Param("id") Long id);

    @Query("""
    SELECT DISTINCT s
    FROM SideJob s
    LEFT JOIN FETCH s.missions m
    LEFT JOIN FETCH m.steps st
    WHERE s.id = :id
""")
    Optional<SideJob> findByIdWithMissionsAndSteps(@Param("id") Long id);

    boolean existsByUserId(Long userId);

    List<SideJob> findAllByUserId(Long userId);

    List<SideJob> findTop3ByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT s.id FROM SideJob s WHERE s.userId = :userId AND s.isSelected = true")
    Optional<Long> findSelectedSideJobByUserId(Long userId);
}
