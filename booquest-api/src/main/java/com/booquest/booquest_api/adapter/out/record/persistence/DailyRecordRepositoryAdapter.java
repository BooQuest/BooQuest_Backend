package com.booquest.booquest_api.adapter.out.record.persistence;

import com.booquest.booquest_api.application.port.out.record.DailyRecordRepositoryPort;
import com.booquest.booquest_api.domain.record.model.DailyRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DailyRecordRepositoryAdapter implements DailyRecordRepositoryPort {

    private final DailyRecordRepository dailyRecordJpaRepository;

    @Override
    public boolean existsByUserIdAndDate(Long userId, LocalDate date) {
        return false;
    }

    @Override
    public long countByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to) {
        return 0;
    }

    @Override
    public List<LocalDate> findAllRecordDatesByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to) {
        List<LocalDate> dates = dailyRecordJpaRepository.findAllRecordDatesByUserIdAndRecordDateBetween(userId, from, to);
        return dates != null ? dates : List.of();
    }

    @Override
    public DailyRecord save(DailyRecord record) {
        return dailyRecordJpaRepository.save(record);
    }

    @Override
    public Optional<DailyRecord> findByUserIdAndRecordDate(Long userId, LocalDate recordDate) {
        return dailyRecordJpaRepository.findByUserIdAndRecordDate(userId, recordDate);
    }

    @Override
    public List<DailyRecord> findByUserIdAndRecordDateBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return dailyRecordJpaRepository.findByUserIdAndRecordDateBetween(userId, startDate, endDate);
    }

    @Override
    public boolean existsByUserIdAndRecordDate(Long userId, LocalDate recordDate) {
        return dailyRecordJpaRepository.existsByUserIdAndRecordDate(userId, recordDate);
    }

    @Override
    public List<DailyRecord> findByUserIdOrderByRecordDateDesc(Long userId, int limit) {
        return dailyRecordJpaRepository.findByUserIdOrderByRecordDateDesc(userId, limit);
    }
}
