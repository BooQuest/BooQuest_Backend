package com.booquest.booquest_api.application.port.in.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeListResponse;

public interface GetIncomeListUseCase {
    IncomeListResponse getIncomeList(Long userId, Long userSideJobId);
}
