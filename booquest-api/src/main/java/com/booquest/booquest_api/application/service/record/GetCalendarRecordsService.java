package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.CalendarDayRecord;
import com.booquest.booquest_api.adapter.in.record.web.dto.CalendarRecordResponse;
import com.booquest.booquest_api.application.port.in.record.GetCalendarRecordsUseCase;
import com.booquest.booquest_api.application.port.out.record.DailyRecordRepositoryPort;
import com.booquest.booquest_api.domain.record.model.DailyRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            DailyRecord rec = byDate.get(date);

            boolean hasRecord = rec != null;
            boolean xpGranted = rec != null && rec.isXpGranted();
            String imageUrl   = rec != null ? rec.getImageUrl() : null;

            days.add(new CalendarDayRecord(day, hasRecord, imageUrl, xpGranted));
        }

        return new CalendarRecordResponse(year, month, days);
    }
}
