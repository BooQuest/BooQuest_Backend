package com.booquest.booquest_api.application.port.in.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeSummaryResponse;

public interface GetIncomeSummaryUseCase {
    IncomeSummaryResponse getIncomeSummary(Long userId);
}
