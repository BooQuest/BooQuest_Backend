package com.booquest.booquest_api.adapter.out.ai;

import com.booquest.booquest_api.application.dto.SideJobGenerationResult;
import com.booquest.booquest_api.application.port.out.ai.AiClientSideJobPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class SyncAiClient implements AiClientSideJobPort {

    private static final String FAST_API_URL = "http://0.0.0.0:8000/ai/generate-side-job";

    private final WebClient webClient = WebClient.builder()
            .baseUrl(FAST_API_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    record OnboardingProfileRequest(String job, List<String> hobbies, String personality) {}

    @Override
    public SideJobGenerationResult generateSideJob(String job, List<String> hobbies) {
        OnboardingProfileRequest requestBody = new OnboardingProfileRequest(
                job,
                hobbies,
                "진지하고 분석적인 성격" // 필요 시 동적으로 변경
        );

        try {
            return webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(SideJobGenerationResult.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("AI 서버 응답 실패: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("AI 서버 호출 실패", e);
        }
    }
}
