package com.booquest.booquest_api.domain.bonus.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum ProofType {
    LINK, TEXT, IMAGE;

    @JsonCreator
    public static ProofType from(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toUpperCase(Locale.ROOT);
        return switch (s) {
            case "LINK", "URL" -> LINK;
            case "TEXT", "TXT" -> TEXT;
            case "IMAGE", "IMG", "PHOTO", "PICTURE" -> IMAGE;
            default -> throw new IllegalArgumentException("Unsupported proofType: " + raw);
        };
    }

    @JsonValue
    public String toJson() { return name(); }
}