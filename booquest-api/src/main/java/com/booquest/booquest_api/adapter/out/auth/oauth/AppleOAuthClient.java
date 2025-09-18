package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.adapter.out.auth.oauth.dto.AppleTokenResponse;
import com.booquest.booquest_api.application.port.out.auth.OAuthClientPort;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import com.booquest.booquest_api.domain.user.model.SocialUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

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

    @Value("${apple.auth.client-secret}")
    private String clientSecret;

    private final AppleClientSecretGenerator secretGenerator;

    private final WebClient webClient;

    @Override
    public SocialUser fetchUserInfo(String code) {
        try {
//            String clientSecret = secretGenerator.generate();
//            log.info("Apple client_secret = {}", clientSecret);

            log.info("client_id: {}", clientId);
            log.info("code: {}", code);
            log.info("client_secret: {}", clientSecret);

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
                    .doOnError(e -> {
                        log.error(e.getMessage(), e);
                    })
                    .block();
            } catch (WebClientResponseException e) {
                log.error("[AppleOAuth] 토큰 요청 실패: {}", e.getResponseBodyAsString(), e);
                throw new RuntimeException("Apple 로그인 실패 - 토큰 요청 실패", e);
            }

            SignedJWT signedJWT = SignedJWT.parse(tokenResponse.getIdToken());
            // 서명 검증 안 할 경우
            // JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // 서명 검증 할 경우
            JWTClaimsSet claims;
            try {
                // JWK 소스 만들 때 URL은 checked exception이므로 별도 try-catch 필요
                URL jwkSetUrl = new URL("https://appleid.apple.com/auth/keys");
                JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(jwkSetUrl);

                // 토큰 헤더의 알고리즘 사용 (RS256/ES256 등)
                JWSAlgorithm alg = (JWSAlgorithm) signedJWT.getHeader().getAlgorithm();

                // 알고리즘 전용 KeySelector
                JWSKeySelector<SecurityContext> keySelector =
                        new JWSVerificationKeySelector<>(alg, keySource);

                var processor = new DefaultJWTProcessor<SecurityContext>();
                processor.setJWSKeySelector(keySelector);

                claims = processor.process(signedJWT, null);
            } catch (java.net.MalformedURLException e) {
                log.error("Apple 공개키 URL 오류", e);
                throw new RuntimeException("Apple 로그인 실패 - 공개키 URL 오류", e);
            } catch (com.nimbusds.jose.proc.BadJOSEException e) {
                log.error("Apple JWT 서명/규격 검증 실패", e);
                throw new RuntimeException("Apple 로그인 실패 - 서명 검증 실패", e);
            } catch (JOSEException e) {
                log.error("Apple JWT 처리 실패 (JOSEException)", e);
                throw new RuntimeException("Apple 로그인 실패 - JOSE 처리 오류", e);
            }

            // (선택) iss/aud/exp 등의 기본 클레임 검증을 여기서 추가 가능
            // iss
            if (!"https://appleid.apple.com".equals(claims.getIssuer())) throw new RuntimeException("Invalid iss");
            // aud
            if (!claims.getAudience().contains(clientId)) throw new RuntimeException("Invalid aud");
            // exp
            if (claims.getExpirationTime() == null || claims.getExpirationTime().before(new Date()))
                throw new RuntimeException("Expired token");
            // (선택) nonce: 로그인 시작 시 생성한 값과 동일한지 비교

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
        } catch (ParseException e) {
            log.error("Apple 로그인 실패 - 토큰 파싱 오류", e);
        } catch (RuntimeException e) {
            log.error("Apple 로그인 실패 - 통신 또는 처리 오류", e);
        }
        return null; // 또는 throw new CustomOAuthException("Apple 로그인 실패");
    }
}
