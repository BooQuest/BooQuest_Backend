package com.booquest.booquest_api.adapter.in.income.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IncomeSummaryResponse {
    private Long totalIncome;
    private Long completedSideJobCount;
    private Long completedQuestCount;
    private Long totalIncomeCount;
}
