package com.booquest.booquest_api.application.port.out.ai;

import com.booquest.booquest_api.domain.model.ai.AiTaskRequest;
import com.booquest.booquest_api.domain.model.ai.GeneratedTask;

import java.util.List;

/** AI 서버 호출 어댑터가 구현해야 하는 출력 포트 */
public interface AiClientPort {
    List<GeneratedTask> requestTasks(AiTaskRequest request);
}
