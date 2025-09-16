package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.application.port.out.auth.AppleUnlinkPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleUnlinkClient implements AppleUnlinkPort {
    private final WebClient webClient;

    @Value("${apple.auth.client-id}")
    private String clientId;
    private AppleClientSecretGenerator secretGenerator;

    @Override
    public boolean revokeToken(String refreshToken) {
        String clientSecret = secretGenerator.generate();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("token", refreshToken);
        formData.add("token_type_hint", "refresh_token");

        try {
            webClient.post()
                    .uri("https://appleid.apple.com/auth/revoke")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (Exception e) {
            log.error("Apple refresh_token revoke 실패", e);
            return false;
        }
    }
}
