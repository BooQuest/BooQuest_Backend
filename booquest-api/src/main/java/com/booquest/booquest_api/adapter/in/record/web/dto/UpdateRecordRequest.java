package com.booquest.booquest_api.adapter.in.record.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateRecordRequest {
    @Schema(description = "기록 내용", example = "오늘은 프리랜서 디자인 작업을 완료했습니다.")
    @Size(max = 1000, message = "기록 내용은 1000자를 초과할 수 없습니다.")
    String content;

    @Schema(description = "이미지 File")
    private MultipartFile file;

    @Schema(description = "기존 이미지를 지울 때 true로 보냅니다. true면 OCI에서도 삭제되고 DB에서도 이미지가 비워집니다. file이 와도 이 값이 우선합니다.", example = "false")
    private Boolean removeImage;
}
