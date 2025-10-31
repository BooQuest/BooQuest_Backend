package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class CreateRecordRequest {
    @Schema(description = "기록 내용", example = "오늘은 프리랜서 디자인 작업을 완료했습니다.")
    @Size(max = 1000, message = "기록 내용은 1000자를 초과할 수 없습니다.")
    String content;

    @Schema(description = "이미지 File")
    private MultipartFile file;
}
