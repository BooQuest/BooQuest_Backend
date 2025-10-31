package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RecordSummaryResponse {
    @Schema(description = "이번 주 기록 여부 (일~토)")
    private final List<DayChip> dayChips;
}
