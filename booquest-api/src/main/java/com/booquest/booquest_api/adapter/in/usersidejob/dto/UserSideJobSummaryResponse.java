package com.booquest.booquest_api.adapter.in.usersidejob.dto;

import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class UserSideJobSummaryResponse {
    //부업, 기간, 카테고리, 나의 총 수익, 완료한 퀘스트 수, 첫 수익화까지(일)
    private final UserSideJob userSideJob;
    private final String period;                 // ex. 2025.08.01 ~ 2025.09.01 / 2025.08.01 ~ 진행중
    private final String category;
    private final BigDecimal totalRevenue;       // 나의 총 수익
    private final Integer completedQuestCount;   // 완료한 퀘스트 수
    private final Integer daysToFirstRevenue;    // 첫 수익화까지(일)

    private static final DateTimeFormatter D = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static UserSideJobSummaryResponse toResponse(UserSideJob userSideJob,
                                                        String category,
                                                        BigDecimal totalRevenue,
                                                        Integer completedQuestCount,
                                                        Integer daysToFirstRevenue) {
        return UserSideJobSummaryResponse.builder()
                .userSideJob(userSideJob)
                .period(calculatePeriod(userSideJob.getStartedAt(), userSideJob.getEndedAt()))
                .category(category)
                .totalRevenue(totalRevenue)
                .completedQuestCount(completedQuestCount)
                .daysToFirstRevenue(daysToFirstRevenue)
                .build();
    }

    private static String calculatePeriod(LocalDateTime startedAt, LocalDateTime endedAt) {
        // yyyy.MM.dd ~ yyyy.MM.dd / yyyy.MM.dd ~ 진행중 / 시작일 없으면 "-"
        if (startedAt == null) return "-";
        String start = D.format(startedAt);
        if (endedAt != null) {
            return start + " ~ " + D.format(endedAt);
        }
        return start + " ~ 진행중";
    }
}
