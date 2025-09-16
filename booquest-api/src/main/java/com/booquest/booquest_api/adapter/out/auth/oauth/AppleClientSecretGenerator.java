package com.booquest.booquest_api.adapter.out.auth.oauth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AppleClientSecretGenerator {

    @Value("${apple.auth.team-id}")
    private String teamId;

    @Value("${apple.auth.key-id}")
    private String keyId;

    @Value("${apple.auth.client-id}")
    private String clientId;

    @Value("${apple.auth.private-key}")
    private String privateKey;

    public String generate() {
        try {
            long now = Instant.now().getEpochSecond();
            long exp = now + 86400 * 180; // 180일

            JWSSigner signer = new RSASSASigner(getPrivateKey());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .issuer(teamId)
                    .issueTime(Date.from(Instant.ofEpochSecond(now)))
                    .expirationTime(Date.from(Instant.ofEpochSecond(exp)))
                    .audience("https://appleid.apple.com")
                    .subject(clientId)
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(keyId).build(),
                    claimsSet
            );

            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("[AppleClientSecret] 서명 생성 실패", e);
            return null;
        } catch (Exception e) {
            log.error("[AppleClientSecret] 알 수 없는 오류", e);
            return null;
        }
    }

    private ECPrivateKey getPrivateKey() {
        try {
            String pkcs8Pem = privateKey
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replace("\\n", "")
                    .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(pkcs8Pem);
            KeyFactory kf = KeyFactory.getInstance("EC");
            return (ECPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Apple private key 로드 실패", e);
        }
    }
}

