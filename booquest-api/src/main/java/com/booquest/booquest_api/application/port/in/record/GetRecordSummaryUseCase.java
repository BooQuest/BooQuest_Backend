package com.booquest.booquest_api.application.port.in.record;

import com.booquest.booquest_api.adapter.in.record.web.dto.RecordSummaryResponse;

public interface GetRecordSummaryUseCase {
    /**
     * 홈화면 요약 정보 조회
     */
    RecordSummaryResponse getRecordSummary(Long userId);
}
