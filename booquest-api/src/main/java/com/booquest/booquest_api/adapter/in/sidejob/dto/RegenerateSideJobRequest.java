package com.booquest.booquest_api.adapter.in.sidejob.dto;

import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import com.booquest.booquest_api.domain.onboarding.enums.ExpressionStyle;
import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record RegenerateSideJobRequest(RegenerateFeedbackData feedbackData, @Valid GenerateSideJobRequest generateSideJobRequest) {
    @RequiredArgsConstructor
    @Getter
    public enum FeedbackType{
        LOW_PROFITABILITY("주제가 마음에 들지 않아요"),
        NOT_INTERESTING("플랫폼이 마음에 들지 않아요"),
        TOO_TIME_CONSUMING("시간이 너무 많이 들어요"),
        CHANGE_TO_OTHER("다른걸로 바꿔주세요"),
        NONE("없음");

        private final String label;
        public static FeedbackType from(String input) {
            for (FeedbackType value : FeedbackType.values()) {
                if (value.label.equals(input) || value.name().equalsIgnoreCase(input)) {
                    return value;
                }
            }
            return NONE;
        }

    }

    public record RegenerateFeedbackData(
            List<FeedbackType> reasons,
            String etcFeedback // 사용자가 입력한 기타 사유
    ) {}
}
