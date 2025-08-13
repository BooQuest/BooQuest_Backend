package com.booquest.booquest_api.application.service.ai;

import com.booquest.booquest_api.application.port.in.ai.AiInputPort;
import com.booquest.booquest_api.application.port.out.ai.AiClientPort;
import com.booquest.booquest_api.domain.model.ai.AiTaskRequest;
import com.booquest.booquest_api.domain.model.ai.GeneratedTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AIAssitantService implements AiInputPort {

    private final AiClientPort aiTaskClient;

    @Override
    public List<GeneratedTask> generate(AiTaskRequest request) {

        return aiTaskClient.requestTasks(request);
    }
}
