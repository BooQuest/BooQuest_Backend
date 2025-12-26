package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CalendarRecordResponse {
    @Schema(description = "년도")
    int year;

    @Schema(description = "월")
    int month;

    @Schema(description = "일별 기록 현황")
    List<CalendarDayRecord> records;
}
