package com.booquest.booquest_api.adapter.in.missionstep.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;

public record RegenerateMissionStepRequest(RegenerateFeedbackData feedbackData, MissionStepGenerateRequestDto generateMissionStep) {

    @RequiredArgsConstructor
    public enum MissionStepFeedbackType {
        TOO_DIFFICULT("너무 어려워요"),
        TOO_EASY("너무 쉬워요"),
        NOT_MY_CHOICE("제가 선택한 부업과 맞지 않아요"),
        NOT_MATCHING_GOAL("목표와 어울리지 않아요"),
        NONE("없음");

        private final String displayName;

        public static MissionStepFeedbackType from(String input) {
            for (MissionStepFeedbackType value : MissionStepFeedbackType.values()) {
                if (value.displayName.equals(input) || value.name().equalsIgnoreCase(input)) {
                    return value;
                }
            }
            return NONE;
        }
    }

    public record RegenerateFeedbackData(
            List<MissionStepFeedbackType> reasons,
            String etcFeedback // 사용자가 입력한 기타 사유
    ) {}
}
