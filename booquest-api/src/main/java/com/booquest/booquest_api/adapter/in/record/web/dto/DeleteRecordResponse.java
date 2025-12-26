package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class DeleteRecordResponse {
    @Schema(description = "기록 ID")
    Long id;

    @Schema(description = "기록 날짜")
    String recordDate;
}
