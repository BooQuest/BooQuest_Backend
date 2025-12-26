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

    private final DailyRecordRepository dailyRecordRepository;

    @Override
    public Optional<DailyRecord> findByIdAndUserId(Long recordId, Long userId) {
        return dailyRecordRepository.findByIdAndUserId(recordId, userId);
    }

    @Override
    public List<LocalDate> findAllRecordDatesByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to) {
        List<LocalDate> dates = dailyRecordRepository.findAllRecordDatesByUserIdAndRecordDateBetween(userId, from, to);
        return dates != null ? dates : List.of();
    }

    @Override
    public DailyRecord save(DailyRecord record) {
        return dailyRecordRepository.save(record);
    }

    @Override
    public Optional<DailyRecord> findByUserIdAndRecordDate(Long userId, LocalDate recordDate) {
        return dailyRecordRepository.findByUserIdAndRecordDate(userId, recordDate);
    }

    @Override
    public List<DailyRecord> findByUserIdAndRecordDateBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return dailyRecordRepository.findByUserIdAndRecordDateBetween(userId, startDate, endDate);
    }
}
