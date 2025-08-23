package com.booquest.booquest_api.adapter.out.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.missionstep.GenerateMissionStepResult;
import com.booquest.booquest_api.application.port.out.missionstep.GenerateMissionStepPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class AiMissionStepGenerator implements GenerateMissionStepPort {

    private static final String API_URL = "/ai/generate-mission-step";
    private final @Qualifier("aiWebClient") WebClient webClient;

    @Override
    public GenerateMissionStepResult generateMissionStep(MissionStepGenerateRequestDto request) {
        try {
            GenerateMissionStepResult result = webClient.post()
                    .uri(API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GenerateMissionStepResult.class)
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
        } catch (Exception e) {
            throw new RuntimeException("AI 서버 호출 실패", e);
        }
    }
}
