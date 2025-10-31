package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.DailyRecordResponse;
import com.booquest.booquest_api.application.port.in.character.UpdateCharacterExpUseCase;
import com.booquest.booquest_api.application.port.in.record.GetRecordUseCase;
import com.booquest.booquest_api.application.port.in.storage.ImageStoragePort;
import com.booquest.booquest_api.application.port.out.record.DailyRecordRepositoryPort;
import com.booquest.booquest_api.domain.record.model.DailyRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GetRecordService implements GetRecordUseCase {
    private final DailyRecordRepositoryPort dailyRecordRepository;
    private final ImageStoragePort imageStoragePort;
    private final UpdateCharacterExpUseCase updateCharacterExpUseCase;

    private static final int DAILY_RECORD_XP = 5;

    @Override
    @Transactional(readOnly = true)
    public DailyRecordResponse getRecordByDate(Long userId, LocalDate date) {
        Optional<DailyRecord> record = dailyRecordRepository.findByUserIdAndRecordDate(userId, date);

        if (record.isEmpty()) {
            return new DailyRecordResponse(null, date.toString(), null, null, false, 0);
        }

        DailyRecord dailyRecord = record.get();
        int xpAmount = dailyRecord.isXpGranted() ? DAILY_RECORD_XP : 0;

        return new DailyRecordResponse(
                dailyRecord.getId(),
                dailyRecord.getRecordDate().toString(),
                dailyRecord.getContent(),
                dailyRecord.getImageUrl(),
                dailyRecord.isXpGranted(),
                xpAmount
        );
    }
}
