package com.booquest.booquest_api.domain.character.enums;

import java.util.Arrays;

public enum CharacterType {
    BLACK, WHITE;


    public static CharacterType from(String input) {
        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("캐릭터 타입이 일치하지 않습니다. (BLACK, WHITE) " + input));
    }
}
