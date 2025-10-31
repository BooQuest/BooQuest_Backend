package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CalendarDayRecord {
    @Schema(description = "일")
    private final int day;

    @Schema(description = "기록 존재 여부")
    private final boolean hasRecord;

    @Schema(description = "기록 이미지 미리보기 URL (없을 수도 있음)")
    private final String imageUrl;

    @Schema(description = "XP 지급 여부")
    private final boolean xpGranted;
}
