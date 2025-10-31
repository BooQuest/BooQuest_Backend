package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateRecordRequest {
    @Schema(description = "기록 내용", example = "오늘은 프리랜서 디자인 작업을 완료했습니다.")
    @Size(max = 1000, message = "기록 내용은 1000자를 초과할 수 없습니다.")
    String content;

//    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
//    String imageUrl;
    @Schema(description = "이미지 Object Key", example = "record/u123/2025/10/30/IMG_1234_thumb.jpg")
    private String objectKey;
}
