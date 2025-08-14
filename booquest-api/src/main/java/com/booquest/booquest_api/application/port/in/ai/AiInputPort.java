package com.booquest.booquest_api.application.port.in.ai;

import com.booquest.booquest_api.domain.model.ai.AiTaskRequest;
import com.booquest.booquest_api.domain.model.ai.GeneratedTask;

import java.util.List;

/**
 * 애플리케이션 계층에서 AI 태스크 생성을 위한 입력 포트.
 * Controller -> Service 의 진입점 역할을 합니다.
 */
public interface AiInputPort {
    List<GeneratedTask> generate(AiTaskRequest request);
}
