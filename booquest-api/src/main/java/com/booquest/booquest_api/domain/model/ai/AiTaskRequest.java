package com.booquest.booquest_api.domain.model.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * AI 태스크 생성 요청 모델.
 * userId는 선택적, context는 필수입니다.
 */
@Getter
@AllArgsConstructor
public class AiTaskRequest {
    private final String userId;
    @NonNull
    private final String context;
}
