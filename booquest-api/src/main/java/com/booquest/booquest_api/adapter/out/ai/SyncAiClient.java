package com.booquest.booquest_api.adapter.out.ai;

import com.booquest.booquest_api.application.dto.SideJobGenerationResult;
import com.booquest.booquest_api.application.port.out.ai.AiClientSideJobPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class SyncAiClient implements AiClientSideJobPort {

    private static final String API_URL = "/ai/generate-side-job";
    private final @Qualifier("aiWebClient") WebClient webClient;

    record OnboardingProfileRequest(String job, List<String> hobbies, String desiredSideJob) {}

    @Override
    public SideJobGenerationResult generateSideJob(String job, List<String> hobbies, String desiredSideJob) {
        OnboardingProfileRequest requestBody = new OnboardingProfileRequest(
                job, hobbies, desiredSideJob
        );

        try {
            return webClient.post()
                    .uri(API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
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
