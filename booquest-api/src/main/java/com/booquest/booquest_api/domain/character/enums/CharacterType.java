package com.booquest.booquest_api.domain.character.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Arrays;


@JsonSerialize(using = ToStringSerializer.class)
public enum CharacterType {
    BLACK, WHITE;


    public static CharacterType from(String input) {
        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("캐릭터 타입이 일치하지 않습니다. (BLACK, WHITE) " + input));
    }
}
