package com.booquest.booquest_api.domain.model.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AI 서버가 생성해 준 태스크 도메인 모델.
 * priority는 숫자/문자 등 다양한 타입이 섞일 수 있어 Object로 둡니다.
 */
@Data
@AllArgsConstructor
public class GeneratedTask {
    private String title;
    private String description;
    private Object priority;
    private String difficulty;
}
