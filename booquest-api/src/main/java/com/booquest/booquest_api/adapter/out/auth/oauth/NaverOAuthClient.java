package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.adapter.out.auth.oauth.dto.NaverUserResponse;
import com.booquest.booquest_api.application.port.out.auth.OAuthClientPort;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
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
public class NaverOAuthClient implements OAuthClientPort {
    private final WebClient webClient;

    @Value("${naver.api.user-info-url}") 
    private String userInfoUrl;

    @Override
    public SocialUser fetchUserInfo(String accessToken) {
        NaverUserResponse response = webClient.get()
                .uri(userInfoUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        res -> {
                            log.error("네이버 API 호출 실패: {}", res.statusCode());
                            return Mono.error(new RuntimeException("네이버 사용자 정보 조회 실패"));
                        })
                .bodyToMono(NaverUserResponse.class)
                .block();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(response);
            log.info("네이버 API 응답(JSON): {}", jsonString);
        } catch (Exception e) {
            log.error("네이버 API 응답 JSON 변환 실패", e);
        }

        if (response == null || response.getResponse() == null) {
            throw new RuntimeException("네이버 사용자 정보 응답이 올바르지 않습니다");
        }

        NaverUserResponse.Response userInfo = response.getResponse();

        return SocialUser.builder()
                .email(userInfo.getEmail())
                .nickname(userInfo.getNickname())
                .provider(AuthProvider.NAVER)
                .providerId(userInfo.getId())
                .profileImageUrl(userInfo.getProfileImage())
                .build();
    }
}