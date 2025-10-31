package com.booquest.booquest_api.adapter.out.record.persistence;

import com.booquest.booquest_api.domain.record.model.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {

    Optional<DailyRecord> findByIdAndUserId(Long recordId, Long userId);

    Optional<DailyRecord> findByUserIdAndRecordDate(Long userId, LocalDate recordDate);
    
    List<DailyRecord> findByUserIdAndRecordDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("""
        select d.recordDate
        from DailyRecord d
        where d.userId = :userId
          and d.recordDate between :from and :to
        """)
    List<LocalDate> findAllRecordDatesByUserIdAndRecordDateBetween(
            @Param("userId") Long userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
    
    boolean existsByUserIdAndRecordDate(Long userId, LocalDate recordDate);
    
    @Query("SELECT dr FROM DailyRecord dr WHERE dr.userId = :userId ORDER BY dr.recordDate DESC")
    List<DailyRecord> findByUserIdOrderByRecordDateDesc(@Param("userId") Long userId, org.springframework.data.domain.Pageable pageable);
    
    default List<DailyRecord> findByUserIdOrderByRecordDateDesc(Long userId, int limit) {
        return findByUserIdOrderByRecordDateDesc(userId, org.springframework.data.domain.PageRequest.of(0, limit));
    }
}
