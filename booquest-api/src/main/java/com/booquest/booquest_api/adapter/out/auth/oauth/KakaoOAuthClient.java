package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.adapter.out.auth.oauth.dto.KakaoUserResponse;
import com.booquest.booquest_api.application.port.out.auth.OAuthClientPort;
import com.booquest.booquest_api.domain.user.model.SocialUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClientPort {
    private final WebClient webClient;

    @Value("${kakao.api.user-info-url}") String userInfoUrl;

    @Override
    public SocialUser fetchUserInfo(String accessToken) {
        KakaoUserResponse response = webClient.get()
                .uri(userInfoUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        res -> {
                            log.error("카카오 API 호출 실패: {}", res.statusCode());
                            return Mono.error(new RuntimeException("카카오 사용자 정보 조회 실패"));
                        })
                .bodyToMono(KakaoUserResponse.class)
                .block();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(response);
            log.info("카카오 API 응답(JSON): {}", jsonString);
        } catch (Exception e) {
            log.error("카카오 API 응답 JSON 변환 실패", e);
        }

        return SocialUser.builder()
                .email(response.getKakaoAccount().getEmail())
                .nickname(response.getKakaoAccount().getProfile().getNickname())
                .provider("KAKAO")
                .providerId(String.valueOf(response.getId()))
                .profileImageUrl(response.getKakaoAccount().getProfile().getProfileImageUrl())
                .build();
    }
}
