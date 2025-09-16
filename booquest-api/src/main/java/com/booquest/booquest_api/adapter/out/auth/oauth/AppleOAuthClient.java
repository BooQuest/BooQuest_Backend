package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.adapter.out.auth.oauth.dto.AppleTokenResponse;
import com.booquest.booquest_api.application.port.out.auth.OAuthClientPort;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import com.booquest.booquest_api.domain.user.model.SocialUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.URL;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleOAuthClient implements OAuthClientPort {

    @Value("${apple.auth.client-id}")
    private String clientId;

    private final AppleClientSecretGenerator secretGenerator;

    private final WebClient webClient;

    @Override
    public SocialUser fetchUserInfo(String code) {
        try {
            String clientSecret = secretGenerator.generate();
            log.info("Apple client_secret = {}", clientSecret);

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", "authorization_code");
            formData.add("code", code);
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);

            AppleTokenResponse tokenResponse;
            try {
            tokenResponse = webClient.post()
                    .uri("https://appleid.apple.com/auth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(AppleTokenResponse.class)
                    .block();
            } catch (WebClientResponseException e) {
                log.error("[AppleOAuth] 토큰 요청 실패: {}", e.getResponseBodyAsString(), e);
                throw new RuntimeException("Apple 로그인 실패 - 토큰 요청 실패", e);
            }

            SignedJWT signedJWT = SignedJWT.parse(tokenResponse.getIdToken());
            var claims = new DefaultJWTProcessor<SecurityContext>()
                    .process(signedJWT, null);

            String email = claims.getStringClaim("email");
            String userId = claims.getSubject();
            String refreshToken = tokenResponse.getRefreshToken();

            log.info("Apple 로그인 완료: sub={}, email={}", userId, email);

            return SocialUser.builder()
                    .email(email)
                    .nickname(null)
                    .provider(AuthProvider.APPLE)
                    .providerId(userId)
                    .profileImageUrl(null)
                    .refreshToken(refreshToken)
                    .build();
        } catch (JOSEException | ParseException | BadJOSEException e) {
            log.error("Apple 로그인 실패 - 토큰 파싱 오류", e);
        } catch (RuntimeException e) {
            log.error("Apple 로그인 실패 - 통신 또는 처리 오류", e);
        }
        return null; // 또는 throw new CustomOAuthException("Apple 로그인 실패");
    }
}
