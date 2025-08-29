package com.booquest.booquest_api.domain.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Locale;


@JsonSerialize(using = ToStringSerializer.class)
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
