package com.booquest.booquest_api.adapter.out.ai;

import com.booquest.booquest_api.application.dto.SideJobGenerationResult;
import com.booquest.booquest_api.application.port.out.ai.AiClientSideJobPort;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class FakeAiClient implements AiClientSideJobPort {

    private final RestTemplate restTemplate;

    private static final String FAST_API_URL = "http://0.0.0.0:8000/ai/generate-tasks";

    @Override
    public SideJobGenerationResult generateSideJob(String job, List<String> hobbies) {
        // 요청 바디 생성
        Map<String, Object> requestBody = Map.of(
                "personality", "열정적이고 창의적인 문제 해결사",
                "selected_side_hustle", job,
                "characteristics", List.of("자기 주도적", "도전 정신 강함", "끈기 있음"),
                "hobbies", hobbies,
                "skills", List.of("Python", "FastAPI", "LangChain", "Prompt Engineering"),
                "experience_level", "초급"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<SideJobGenerationResult> response = restTemplate.postForEntity(
                    FAST_API_URL,
                    requestEntity,
                    SideJobGenerationResult.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("AI 서버 요청 실패: " + e.getMessage(), e);
        }
    }
}
