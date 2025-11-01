package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.CalendarDayRecord;
import com.booquest.booquest_api.adapter.in.record.web.dto.CalendarRecordResponse;
import com.booquest.booquest_api.application.port.in.record.GetCalendarRecordsUseCase;
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
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GetCalendarRecordsService implements GetCalendarRecordsUseCase {
    private final DailyRecordRepositoryPort dailyRecordRepository;
    private final ImageStoragePort imageStoragePort;

    private static final Duration PRESIGNED_TTL = Duration.ofMinutes(10);
    private static final Duration RENEW_THRESHOLD = Duration.ofSeconds(30);

    @Override
    @Transactional(readOnly = true)
    public CalendarRecordResponse getCalendarRecords(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate monthStart = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();

        List<DailyRecord> records = dailyRecordRepository.findByUserIdAndRecordDateBetween(userId, monthStart, monthEnd);

        // 날짜 -> Record 매핑 (하루 1건 가정)
        Map<LocalDate, DailyRecord> byDate = records.stream()
                .collect(Collectors.toMap(DailyRecord::getRecordDate, r -> r));

        List<CalendarDayRecord> days = new ArrayList<>(yearMonth.lengthOfMonth());
        Instant now = Instant.now();

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            DailyRecord rec = byDate.get(date);

            boolean hasRecord = rec != null;
            boolean xpGranted = rec != null && rec.isXpGranted();
            String presignedUrl = null;

            if (rec != null && rec.getImageObjectKey() != null) {
                // presigned가 유효한지 확인
                boolean needRenew = needRenew(rec.getImagePresignedUrl(), rec.getImagePresignedExpiresAt(), now);

                if (needRenew) {
                    // 새로 발급
                    String newUrl = imageStoragePort.createPresignedGetUrl(rec.getImageObjectKey(), PRESIGNED_TTL);
                    Instant expiresAt = now.plus(PRESIGNED_TTL);

                    // 엔티티에 반영
                    rec.refreshImagePresigned(newUrl, expiresAt);
                    // 캘린더지만, presigned를 다시 저장해줘야 다음 요청에서도 그대로 씀
                    dailyRecordRepository.save(rec);

                    presignedUrl = newUrl;
                } else {
                    presignedUrl = rec.getImagePresignedUrl();
                }
            }

            days.add(new CalendarDayRecord(day, hasRecord, presignedUrl, xpGranted));
        }

        return new CalendarRecordResponse(year, month, days);
    }

    private boolean needRenew(String presignedUrl, Instant expiresAt, Instant now) {
        if (presignedUrl == null || expiresAt == null) {
            return true;
        }
        // 만료되었거나 (expiresAt <= now) / 남은 시간이 30초 미만이면 재발급
        Instant threshold = now.plus(RENEW_THRESHOLD);
        return expiresAt.isBefore(threshold);
    }
}
