package com.booquest.booquest_api.application.port.out.record;

import com.booquest.booquest_api.domain.record.model.DailyRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyRecordRepositoryPort {
    Optional<DailyRecord> findByIdAndUserId(Long recordId, Long userId);

    /**
     * 특정 기간 내 '기록이 존재하는 날짜'만 간단 Projection으로 조회
     * (중복 제거 위해 DISTINCT)
     */
    @Query("select distinct r.date from Record r " +
            "where r.userId = :userId and r.date between :from and :to")
    List<LocalDate> findAllRecordDatesByUserIdAndDateBetween(@Param("userId") Long userId,
                                                             @Param("from") LocalDate from,
                                                             @Param("to") LocalDate to);
    
    DailyRecord save(DailyRecord record);
    
    Optional<DailyRecord> findByUserIdAndRecordDate(Long userId, LocalDate recordDate);
    
    List<DailyRecord> findByUserIdAndRecordDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
