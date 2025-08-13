package com.booquest.booquest_api.adapter.out.ai;

import com.booquest.booquest_api.adapter.out.ai.dto.AiUserProfileDtos;
import com.booquest.booquest_api.application.port.out.ai.AiClientPort;
import com.booquest.booquest_api.domain.model.ai.AiTaskRequest;
import com.booquest.booquest_api.domain.model.ai.GeneratedTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class AiClient implements AiClientPort {

    private final @Qualifier("aiWebClient") WebClient aiTaskWebClient;

    @Override
    public List<GeneratedTask> requestTasks(AiTaskRequest request) {
        AiUserProfileDtos.Request body = AiUserProfileDtos.Request.builder()
                .personality("INTJ")
                .selectedSideHustle("블로그 운영")
                .characteristics(List.of("분석적", "독립적"))
                .hobbies(List.of("독서", "등산"))
                .skills(List.of("Python", "FastAPI"))
                .experienceLevel("중급")
                .build();

        AiUserProfileDtos.Response resp = aiTaskWebClient.post()
                // FastAPI 라우터가 "/ai" prefix를 사용함
                .uri("/ai/generate-tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AiUserProfileDtos.Response.class)
                .block();

        if (resp == null) {
            throw new IllegalStateException("AI server returned null response");
        }
        if (!resp.isSuccess()) {
            throw new IllegalStateException("AI server failed: " + resp.getMessage());
        }
        if (resp.getBigTasks() == null || resp.getBigTasks().isEmpty()) {
            throw new IllegalStateException("AI server returned empty big_tasks");
        }

        return resp.getBigTasks().stream()
                .map(task -> new GeneratedTask(
                        asString(firstNonNull(task.get("title"), task.get("name"))),        // title/name 호환
                        asString(firstNonNull(task.get("description"), task.get("desc"))),  // description/desc 호환
                        toInteger(firstNonNull(task.get("priority"), task.get("prio"))),    // priority/prio 호환
                        asString(firstNonNull(task.get("difficulty"), task.get("level")))   // difficulty/level 호환
                ))
                .collect(toList());
    }


    /* ---- 유틸 ---- */
    private static Object firstNonNull(Object a, Object b) { return a != null ? a : b; }

    private static String asString(Object v) {
        return v == null ? null : String.valueOf(v);
    }

    private static Integer toInteger(Object v) {
        if (v == null) return null;
        if (v instanceof Integer i) return i;
        if (v instanceof Long l) return Math.toIntExact(l);
        if (v instanceof Double d) return d.intValue();
        if (v instanceof Float f) return f.intValue();
        var s = v.toString().trim();
        try { return Integer.parseInt(s); } catch (NumberFormatException ignore) { return null; }
    }
}
