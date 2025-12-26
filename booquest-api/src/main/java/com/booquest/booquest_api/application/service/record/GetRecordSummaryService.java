package com.booquest.booquest_api.application.service.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.DayChip;
import com.booquest.booquest_api.adapter.in.record.web.dto.RecordSummaryResponse;
import com.booquest.booquest_api.application.port.in.record.GetRecordSummaryUseCase;
import com.booquest.booquest_api.application.port.out.record.DailyRecordRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GetRecordSummaryService implements GetRecordSummaryUseCase {
    private final DailyRecordRepositoryPort dailyRecordRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public RecordSummaryResponse getRecordSummary(Long userId) {
        LocalDate today = LocalDate.now();

        // 주(일~토) 범위 계산 (SUNDAY 기준으로 시작)
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate weekEnd   = weekStart.plusDays(6);

        // 이번 주에 기록이 있는 '날짜들' 한 번에 조회 (N+1 방지용)
        Set<LocalDate> recordedDatesThisWeek = new HashSet<>(
                dailyRecordRepositoryPort.findAllRecordDatesByUserIdAndDateBetween(userId, weekStart, weekEnd)
        );

        // 요일 칩 데이터 생성
        List<DayChip> week = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            LocalDate d = weekStart.plusDays(i);
            boolean hasRecord = recordedDatesThisWeek.contains(d);
            boolean isToday   = d.equals(today);
            week.add(DayChip.of(d, hasRecord, isToday));
        }

        return new RecordSummaryResponse(week);
    }
}
