package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UpdateRecordResponse {
    @Schema(description = "기록 ID")
    Long id;
}
