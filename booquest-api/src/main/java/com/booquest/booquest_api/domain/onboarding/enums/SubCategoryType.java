package com.booquest.booquest_api.domain.onboarding.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubCategoryType {

    // ECONOMY
    INVESTING(CategoryType.ECONOMY, "재테크"),
    ECONOMICS(CategoryType.ECONOMY, "경제"),
    POLITICS(CategoryType.ECONOMY, "정치"),
    CURRENT_EVENTS(CategoryType.ECONOMY, "시사"),

    // CULTURE
    MUSIC(CategoryType.CULTURE, "음악"),
    FILM(CategoryType.CULTURE, "영화·드라마·애니"),
    ART(CategoryType.CULTURE, "미술"),
    SINGING(CategoryType.CULTURE, "노래"),
    DANCING(CategoryType.CULTURE, "춤"),
    INSTRUMENT(CategoryType.CULTURE, "악기"),

    // BEAUTY
    MAKEUP(CategoryType.BEAUTY, "메이크업"),
    FASHION(CategoryType.BEAUTY, "패션"),
    DIET(CategoryType.BEAUTY, "다이어트"),
    SKINCARE(CategoryType.BEAUTY, "피부 관리"),

    // ENTERTAINMENT
    CELEBS(CategoryType.ENTERTAINMENT, "연예인·인플루언서"),
    SHOWS(CategoryType.ENTERTAINMENT, "예능"),
    MEMES(CategoryType.ENTERTAINMENT, "유머·밈"),
    GLOBAL_SHOWS(CategoryType.ENTERTAINMENT, "해외예능·유튜브"),

    // IT & GAME
    GAMES(CategoryType.IT_GAME, "게임"),
    IT(CategoryType.IT_GAME, "IT"),
    TECH(CategoryType.IT_GAME, "테크(전자기기 등)"),

    // LANGUAGE & TRAVEL
    TRAVEL(CategoryType.LANGUAGE_TRAVEL, "여행"),
    LANGUAGE(CategoryType.LANGUAGE_TRAVEL, "언어 학습"),
    ABROAD(CategoryType.LANGUAGE_TRAVEL, "해외 살이"),

    // EDUCATION
    TEACHING(CategoryType.EDUCATION, "지식전달"),
    SELF_DEV(CategoryType.EDUCATION, "자기계발"),
    STUDY_METHOD(CategoryType.EDUCATION, "학습법"),
    MOTIVATION(CategoryType.EDUCATION, "동기부여"),
    PSYCHOLOGY(CategoryType.EDUCATION, "심리"),

    // FOOD
    COOKING(CategoryType.FOOD, "요리"),
    MUKBANG(CategoryType.FOOD, "먹방"),
    RESTAURANT(CategoryType.FOOD, "맛집"),
    CAFE(CategoryType.FOOD, "카페"),
    DESSERT(CategoryType.FOOD, "디저트"),

    // HEALTH
    EXERCISE(CategoryType.HEALTH, "운동"),
    SPORTS(CategoryType.HEALTH, "스포츠"),
    WELLNESS(CategoryType.HEALTH, "건강"),

    // LIFE
    PARENTING(CategoryType.LIFE, "육아"),
    MARRIAGE(CategoryType.LIFE, "결혼"),
    DATING(CategoryType.LIFE, "연애"),
    RELATIONSHIP(CategoryType.LIFE, "인간관계"),
    PETS(CategoryType.LIFE, "반려동물"),

    // ETC
    REVIEW(CategoryType.ETC, "제품·서비스 리뷰"),
    HISTORY(CategoryType.ETC, "역사"),
    DIY(CategoryType.ETC, "취미 DIY"),
    ENVIRONMENT(CategoryType.ETC, "환경"),
    CLIMATE(CategoryType.ETC, "기후변화"),

    CUSTOM(CategoryType.ETC, "사용자 입력 값");

    private final CategoryType parentCategory;
    private final String displayName;

    public static SubCategoryType from(String displayName) {
        for (SubCategoryType type : SubCategoryType.values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        return CUSTOM;
    }
}
