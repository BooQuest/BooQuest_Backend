package com.booquest.booquest_api.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parse(String json, TypeReference<T> typeRef) {
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }
}
