package com.booquest.booquest_api.domain.onboarding.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExpressionStyle {
    TEXT("글"), IMAGE("그림"), VIDEO("영상"), NONE("없음");

    private final String displayName;

    public static ExpressionStyle from(String displayName) {
        for (ExpressionStyle value : ExpressionStyle.values()) {
            if (value.displayName.equals(displayName)) {
                return value;
            }
        }
        return NONE;
    }
}

