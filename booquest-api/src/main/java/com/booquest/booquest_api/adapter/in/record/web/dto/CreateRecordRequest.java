package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CreateRecordRequest {
    @Schema(description = "기록 내용", example = "오늘은 프리랜서 디자인 작업을 완료했습니다.")
    @Size(max = 1000, message = "기록 내용은 1000자를 초과할 수 없습니다.")
    String content;

    @Schema(description = "이미지 File (선택사항). 이미지를 업로드하지 않을 경우 Swagger에서 'Send empty value'를 체크하지 마세요. \" +\n" +
                            "\"파일이 없을 때는 file 필드를 아예 전송하지 않아야 정상 처리됩니다.")
    private MultipartFile file;

    @Schema(description = "기록 날짜 (선택사항, null이면 오늘 날짜로 생성)", example = "2025-11-10")
    private LocalDate recordDate;
}
