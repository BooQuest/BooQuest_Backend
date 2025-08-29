package com.booquest.booquest_api.domain.onboarding.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonSerialize(using = ToStringSerializer.class)
public enum ExpressionStyle {
    TEXT("글"), IMAGE("그림"), VIDEO("영상"), NONE("없음");

    private final String displayName;

    public static ExpressionStyle from(String input) {
        for (ExpressionStyle value : ExpressionStyle.values()) {
            if (value.displayName.equals(input) || value.name().equalsIgnoreCase(input)) {
                return value;
            }
        }
        return NONE;
    }
}

