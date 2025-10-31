package com.booquest.booquest_api.adapter.in.record.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DayChip {
    private LocalDate date;
    private String dayLabel;
    private boolean hasRecord;
    private boolean today;

    public static DayChip of(LocalDate date, boolean hasRecord, boolean isToday) {
        String[] ko = {"일","월","화","수","목","금","토"};
        String dayLabel = isToday ? "오늘" : ko[date.getDayOfWeek().getValue() % 7];
        return DayChip.builder()
                .date(date)
                .dayLabel(dayLabel)
                .hasRecord(hasRecord)
                .today(isToday)
                .build();
    }
}
