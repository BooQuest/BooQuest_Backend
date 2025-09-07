package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.application.port.out.auth.KakaoUnlinkPort;
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
public class KakaoUnlinkClient implements KakaoUnlinkPort {
    private final WebClient webClient;

    @Value("${oauth.kakao.admin-key}")
    private String adminKey;

    @Value("${oauth.kakao.unlink-url:https://kapi.kakao.com/v1/user/unlink}")
    private String unlinkUrl;

    @Value("${oauth.kakao.timeout-ms:4000}")
    private long timeoutMs;

    @Value("${oauth.kakao.retry.max-attempts:1}")
    private int maxRetryAttempts;

    @Override
    public boolean unlinkByUserId(String kakaoUserId) {
        if (!StringUtils.hasText(kakaoUserId)) {
            log.warn("[KakaoUnlink] kakaoUserId blank");
            return false;
        }
        if (!StringUtils.hasText(adminKey)) {
            log.error("[KakaoUnlink] adminKey not configured");
            return false;
        }
        try {
            log.info("[KakaoUnlink] (AdminKey) unlink userId={}", kakaoUserId);

            webClient.post()
                    .uri(unlinkUrl)
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + adminKey)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters
                            .fromFormData("target_id_type", "user_id")
                            .with("target_id", kakaoUserId))
                    .retrieve()
                    .toBodilessEntity()
                    .retryWhen(Retry.max(maxRetryAttempts)
                            .filter(ex -> ex instanceof WebClientResponseException wex && wex.getStatusCode().is5xxServerError())
                            .transientErrors(true))
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            log.info("[KakaoUnlink] (AdminKey) success userId={}", kakaoUserId);
            return true;
        } catch (WebClientResponseException e) {
            int status = e.getRawStatusCode();
            // 이미 해제(400/404)는 성공으로 간주
            if (status == 400 || status == 404) {
                log.info("[KakaoUnlink] already unlinked ({}), userId={}", status, kakaoUserId);
                return true;
            }
            log.error("[KakaoUnlink] failed status={} body={}", status, e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            log.error("[KakaoUnlink] failed ex={}, userId={}", e.toString(), kakaoUserId);
            return false;
        }
    }

    @Override
    public boolean unlinkByAccessToken(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            log.warn("[KakaoUnlink] accessToken blank");
            return false;
        }
        try {
            log.info("[KakaoUnlink] (Bearer) unlink");

            webClient.post()
                    .uri(unlinkUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .toBodilessEntity()
                    .retryWhen(Retry.max(maxRetryAttempts)
                            .filter(ex -> ex instanceof WebClientResponseException wex && wex.getStatusCode().is5xxServerError())
                            .transientErrors(true))
                    .timeout(Duration.ofMillis(timeoutMs))
                    .block();

            log.info("[KakaoUnlink] (Bearer) success");
            return true;
        } catch (WebClientResponseException e) {
            int status = e.getRawStatusCode();
            if (status == 400 || status == 404) {
                log.info("[KakaoUnlink] already unlinked ({}).", status);
                return true;
            }
            log.error("[KakaoUnlink] failed status={} body={}", status, e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            log.error("[KakaoUnlink] failed ex={}", e.toString());
            return false;
        }
    }
}
