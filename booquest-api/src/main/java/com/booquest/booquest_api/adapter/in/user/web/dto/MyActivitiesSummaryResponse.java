package com.booquest.booquest_api.adapter.in.user.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyActivitiesSummaryResponse {
    Long totalIncome;              // 합계 수익(원)
    Integer completedSideJobCount; // 완료한 부업 프로젝트 수
    Integer completedQuestCount;   // 완료한 부퀘스트 수
}
