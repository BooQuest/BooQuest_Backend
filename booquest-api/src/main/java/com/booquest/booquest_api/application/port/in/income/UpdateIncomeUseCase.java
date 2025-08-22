package com.booquest.booquest_api.application.port.in.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.UpdateIncomeRequest;
import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeResponse;

public interface UpdateIncomeUseCase {
    IncomeResponse updateIncome(Long userId, Long incomeId, UpdateIncomeRequest request);
}
