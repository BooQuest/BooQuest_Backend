package com.booquest.booquest_api.domain.onboarding.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;

@Getter
@JsonSerialize(using = ToStringSerializer.class)
public enum CategoryType {
    ECONOMY("경제·사회·재테크"),
    CULTURE("문화·예술"),
    BEAUTY("뷰티·패션"),
    ENTERTAINMENT("연예·예능·밈"),
    IT_GAME("IT·게임"),
    LANGUAGE_TRAVEL("언어·해외·여행"),
    EDUCATION("교육·심리"),
    FOOD("요리·음식"),
    HEALTH("헬스·건강"),
    LIFE("가족·인간관계·라이프"),
    ETC("기타");

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }
}
