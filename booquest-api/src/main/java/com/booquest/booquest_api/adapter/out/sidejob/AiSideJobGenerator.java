package com.booquest.booquest_api.adapter.out.sidejob;

import com.booquest.booquest_api.adapter.in.sidejob.dto.RegenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.sidejob.SideJobGenerationResult;
import com.booquest.booquest_api.application.port.out.sidejob.GenerateSideJobPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class AiSideJobGenerator implements GenerateSideJobPort {

    private static final String GENERATE_API_URL = "/ai/generate-side-job";
    private static final String REGENERATE_API_URL = "/ai/regenerate-side-job";
    private final @Qualifier("aiWebClient") WebClient webClient;


    @Override
    public SideJobGenerationResult generateSideJobs(GenerateSideJobRequest request) {
        return postForSideJobResult(GENERATE_API_URL, request);
    }

    @Override
    public SideJobGenerationResult regenerateSideJob(RegenerateSideJobRequest request) {
        return postForSideJobResult(REGENERATE_API_URL, request);
    }

    private <T> SideJobGenerationResult postForSideJobResult(String url, T requestBody) {
        try {
            SideJobGenerationResult result = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(SideJobGenerationResult.class)
                    .block();

            if (result == null) {
                throw new RuntimeException("AI 서버로부터 응답이 없습니다.");
            }

            if (!result.success()) {
                throw new RuntimeException("AI 서버 응답 실패: " + result.message());
            }

            return result;
        } catch (WebClientResponseException e) {
            throw new RuntimeException("AI 서버 응답 실패: " + e.getResponseBodyAsString(), e);
        }
    }
}
