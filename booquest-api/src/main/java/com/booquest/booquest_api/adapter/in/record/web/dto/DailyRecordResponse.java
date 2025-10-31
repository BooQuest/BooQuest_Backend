package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class DailyRecordResponse {
    @Schema(description = "기록 ID")
    Long id;

    @Schema(description = "기록 날짜")
    String recordDate;

    @Schema(description = "기록 내용")
    String content;

    @Schema(description = "이미지 Object Key")
    String imageObjectKey;

    @Schema(description = "이미지 조회용 presigned URL")
    String imagePresignedUrl;

    @Schema(description = "XP 지급 여부")
    boolean xpGranted;

    @Schema(description = "지급된 XP 양")
    int xpAmount;
}
