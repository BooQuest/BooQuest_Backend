package com.booquest.booquest_api.domain.onboarding.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrengthType {
    CREATE("창작하기"),
    ORGANIZE("정리·전달하기"),
    SHARE("일상 공유하기"),
    TREND("트렌드 파악하기"),
    NONE("없음");

    private final String displayName;

    public static StrengthType from(String displayName) {
        for (StrengthType value : StrengthType.values()) {
            if (value.displayName.equals(displayName)) {
                return value;
            }
        }
        return NONE;
    }
}
