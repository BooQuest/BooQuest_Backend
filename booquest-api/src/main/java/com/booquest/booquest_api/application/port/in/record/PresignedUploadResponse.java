package com.booquest.booquest_api.application.port.in.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 클라이언트가 Object Storage에 직접 업로드할 때 필요한 정보를 담는 DTO.
 */
@AllArgsConstructor
@Getter
public class PresignedUploadResponse {
    @Schema(description = "업로드할 파일의 Object Key",
            example = "records/123/2025-10-30/uuid.jpg")
    private final String objectKey;

    @Schema(description = "직접 업로드 가능한 Presigned PUT URL")
    private final String uploadUrl;

    @Schema(description = "URL 만료까지 남은 시간 (초 단위)",
            example = "600")
    private final int expiresIn;
}
