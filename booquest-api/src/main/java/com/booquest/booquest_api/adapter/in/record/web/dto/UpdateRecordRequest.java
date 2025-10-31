package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateRecordRequest {
    @Schema(description = "기록 ID")
    Long id;

    @Schema(description = "기록 내용", example = "오늘은 프리랜서 디자인 작업을 완료했습니다.")
    @NotBlank(message = "기록 내용은 필수입니다.")
    @Size(max = 1000, message = "기록 내용은 1000자를 초과할 수 없습니다.")
    String content;

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    String imageUrl;
}
