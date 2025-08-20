package com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto;

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
        LOW_PROFITABILITY("수익성이 낮아보여요"),
        NO_INTEREST("흥미가 생기지 않아요"),
        NOT_MY_STYLE("성향과 맞지 않아요"),
        TAKES_TOO_MUCH_TIME("시간이 너무 많이 필요해요"),
        NOT_FEASIBLE("할 수 있는 일이 아니에요"),
        TOO_EXPENSIVE("초기 비용이 부담돼요"),
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
