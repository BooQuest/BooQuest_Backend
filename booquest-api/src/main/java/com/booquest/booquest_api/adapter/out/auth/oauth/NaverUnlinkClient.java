package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.application.port.out.auth.NaverUnlinkPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverUnlinkClient implements NaverUnlinkPort {
    private final WebClient webClient;

    @Value("${oauth.naver.client-id}")    String clientId;
    @Value("${oauth.naver.client-secret}") String clientSecret;

    // 커스터마이즈 가능하도록 외부화
    @Value("${oauth.naver.token-url:https://nid.naver.com/oauth2.0/token}")
    String tokenUrl;

    @Value("${oauth.naver.timeout-ms:4000}") long timeoutMs;
    @Value("${oauth.naver.retry.max-attempts:1}") int maxRetryAttempts;

    @Override
    public boolean unlinkByAccessToken(String accessToken) {
        if (!StringUtils.hasText(clientId) || !StringUtils.hasText(clientSecret)) {
            log.error("[NaverUnlink] clientId/secret not configured");
            return false;
        }
        if (!StringUtils.hasText(accessToken)) {
            log.warn("[NaverUnlink] accessToken blank");
            return false;
        }

        try {
            String body = webClient.post()
                    .uri(tokenUrl)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("grant_type", "delete")
                            .with("client_id", clientId)
                            .with("client_secret", clientSecret)
                            .with("access_token", accessToken) // "Bearer " 붙이지 말 것
                            .with("service_provider", "NAVER"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry
                            .max(maxRetryAttempts)
                            .filter(ex -> ex instanceof WebClientResponseException wex
                                    && wex.getStatusCode().is5xxServerError()))
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            boolean success = body != null && body.contains("\"success\"");
            log.info("[NaverUnlink] success={}, resp={}", success, body);
            return success;

        } catch (WebClientResponseException e) {
            log.error("[NaverUnlink] failed status={} body={}", e.getRawStatusCode(), e.getResponseBodyAsString());
            return false;

        } catch (Exception e) {
            log.error("[NaverUnlink] failed ex={}", e.toString());
            return false;
        }
    }
}
