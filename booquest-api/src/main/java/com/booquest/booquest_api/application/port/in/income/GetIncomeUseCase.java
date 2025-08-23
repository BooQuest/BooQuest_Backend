package com.booquest.booquest_api.application.port.in.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeResponse;

public interface GetIncomeUseCase {
    IncomeResponse getIncome(Long userId, Long incomeId);
}
