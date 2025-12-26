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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GetRecordService implements GetRecordUseCase {
    private final DailyRecordRepositoryPort dailyRecordRepository;
    private final ImageStoragePort imageStoragePort;

    private static final int DAILY_RECORD_XP = 5;
    private static final Duration PRESIGNED_TTL = Duration.ofMinutes(10);

    @Override
    @Transactional
    public DailyRecordResponse getRecordByDate(Long userId, LocalDate date) {
        Optional<DailyRecord> recordOpt = dailyRecordRepository.findByUserIdAndRecordDate(userId, date);

        if (recordOpt.isEmpty()) {
            return new DailyRecordResponse(null, date.toString(), null, null, null, false, 0);
        }

        DailyRecord record = recordOpt.get();
        int xpAmount = record.isXpGranted() ? DAILY_RECORD_XP : 0;

        // 1) 이미지가 아예 없는 기록이면 그냥 반환
        if (record.getImageObjectKey() == null) {
            return new DailyRecordResponse(
                    record.getId(),
                    record.getRecordDate().toString(),
                    record.getContent(),
                    null,
                    null,
                    record.isXpGranted(),
                    xpAmount
            );
        }

        // 2) presigned 유효성 체크
        String presignedUrl = record.getImagePresignedUrl();
        Instant expiresAt = record.getImagePresignedExpiresAt();
        Instant now = Instant.now();

        boolean needReissue = (presignedUrl == null)
                || (expiresAt == null)
                || expiresAt.isBefore(now.plusSeconds(30)); // 만료 임박 시에도 새로 만들기 (여유 30초)

        if (needReissue) {
            String newPresigned = imageStoragePort.createPresignedGetUrl(
                    record.getImageObjectKey(), PRESIGNED_TTL
            );
            Instant newExpiresAt = now.plus(PRESIGNED_TTL);

            record.refreshImagePresigned(newPresigned, newExpiresAt);
            dailyRecordRepository.save(record); // DB에 다시 저장

            presignedUrl = newPresigned;
            expiresAt = newExpiresAt;
        }

        return new DailyRecordResponse(
                record.getId(),
                record.getRecordDate().toString(),
                record.getContent(),
                record.getImageObjectKey(),
                presignedUrl,
                record.isXpGranted(),
                xpAmount
        );
    }
}
