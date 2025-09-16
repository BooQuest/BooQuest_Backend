package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.booquest.booquest_api.application.port.out.auth.OAuthClientPort;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import com.booquest.booquest_api.domain.user.model.SocialUser;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleOAuthClient implements OAuthClientPort {

    @Value("${apple.api.jwks-uri}")
    private String jwksUri;

    @Override
    public SocialUser fetchUserInfo(String identityToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(identityToken);

            // 공개 키로 서명 검증
            JWSVerificationKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(
                    JWSAlgorithm.RS256, new RemoteJWKSet<>(new URL(jwksUri))
            );

            ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
            jwtProcessor.setJWSKeySelector(keySelector);

            var claims = jwtProcessor.process(signedJWT, null);

            String email = claims.getStringClaim("email");
            String userId = claims.getSubject(); // sub 필드 (고유 식별자)

            log.info("Apple 로그인 응답: email={}, sub={}", email, userId);

            return SocialUser.builder()
                    .email(email)
                    .nickname(null) // 애플은 nickname 미제공
                    .provider(AuthProvider.APPLE)
                    .providerId(userId)
                    .profileImageUrl(null)
                    .build();

        } catch (Exception e) {
            log.error("Apple identity token 처리 중 오류", e);
            throw new RuntimeException("Apple 로그인 실패", e);
        }
    }
}
