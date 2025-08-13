package com.booquest.booquest_api.adapter.out.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * FastAPI의 UserProfileRequest(JSON)와 1:1 매핑되는 Java DTO.
 * - FastAPI는 snake_case 키를 사용하므로 @JsonProperty로 정확히 매핑
 * - 유효성 검사를 위해 jakarta.validation 어노테이션 추가(선택)
 */
public class AiUserProfileDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Request {
        @NotBlank
        private String personality;

        @JsonProperty("selected_side_hustle")
        @NotBlank
        private String selectedSideHustle;

        @NotEmpty @Size(min = 1)
        private List<String> characteristics;

        @NotEmpty @Size(min = 1)
        private List<String> hobbies;

        @NotEmpty @Size(min = 1)
        private List<String> skills;

        @JsonProperty("experience_level")
        @NotBlank
        private String experienceLevel;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private boolean success;

        @JsonProperty("big_tasks")
        private List<Map<String, Object>> bigTasks;

        private String message;
    }
}
