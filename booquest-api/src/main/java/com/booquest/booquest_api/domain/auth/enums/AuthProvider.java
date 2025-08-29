package com.booquest.booquest_api.domain.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum AuthProvider {
    KAKAO, GOOGLE, APPLE, NAVER;

    @JsonCreator
    public static AuthProvider from(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toUpperCase(Locale.ROOT);
        return switch (s) {
            case "KAKAO" -> KAKAO;
            case "GOOGLE" -> GOOGLE;
            case "APPLE" -> APPLE;
            case "NAVER" -> NAVER;
            default -> throw new IllegalArgumentException("Unsupported provider: " + raw);
        };
    }

    @JsonValue
    public String toJson() { return name(); }
}
