package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.DeleteRecordResponse;
import com.booquest.booquest_api.adapter.out.record.persistence.DailyRecordRepository;
import com.booquest.booquest_api.application.port.in.record.DeleteRecordUseCase;
import com.booquest.booquest_api.domain.record.model.DailyRecord;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeleteRecordService implements DeleteRecordUseCase {
    private final DailyRecordRepository dailyRecordRepository;

    @Override
    public DeleteRecordResponse deleteRecord(Long userId, Long recordId) {
        DailyRecord dailyRecord = dailyRecordRepository.findByIdAndUserId(userId, recordId)
                .orElseThrow(() -> new EntityNotFoundException("Income not found: " + recordId));

        dailyRecordRepository.delete(dailyRecord);
        return DeleteRecordResponse.builder()
                .id(recordId)
                .recordDate(dailyRecord.getRecordDate().toString())
                .build();
    }
}
