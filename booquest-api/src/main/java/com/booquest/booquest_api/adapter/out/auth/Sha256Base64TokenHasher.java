package com.booquest.booquest_api.adapter.out.auth;

import com.booquest.booquest_api.application.port.out.auth.TokenHashingPort;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class Sha256Base64TokenHasher implements TokenHashingPort {
    @Override
    public String sha256Base64(String input) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(input.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
