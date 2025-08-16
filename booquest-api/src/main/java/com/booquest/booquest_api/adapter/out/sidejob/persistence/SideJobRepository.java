package com.booquest.booquest_api.adapter.out.sidejob.persistence;


import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SideJobRepository extends JpaRepository<SideJob, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE SideJob s SET s.isSelected = false WHERE s.id = :id")
    int updateSelectedFalse(@Param("id") Long id);
}
