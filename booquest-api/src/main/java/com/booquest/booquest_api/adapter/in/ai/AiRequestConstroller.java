package com.booquest.booquest_api.adapter.in.ai;

import com.booquest.booquest_api.application.port.in.ai.AiInputPort;
import com.booquest.booquest_api.domain.model.ai.AiTaskRequest;
import com.booquest.booquest_api.domain.model.ai.GeneratedTask;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/ai")
public class AiRequestConstroller {

    /** 클라이언트가 보내는 요청 바디 */
    private record GenerateTasksRequest(String userId, @NotBlank String context) {}
    /** 클라이언트로 내려보낼 응답 바디 */
    private record GenerateTasksResponse(List<GeneratedTask> tasks) {}

    private final AiInputPort service;

    @PostMapping("/generate-tasks")
    public GenerateTasksResponse generate(@RequestBody GenerateTasksRequest req) {
        List<GeneratedTask> tasks = service.generate(new AiTaskRequest(req.userId(), req.context()));
        return new GenerateTasksResponse(tasks);
    }
}
