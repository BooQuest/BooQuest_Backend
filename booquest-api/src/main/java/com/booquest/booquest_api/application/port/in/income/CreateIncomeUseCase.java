package com.booquest.booquest_api.application.port.in.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.CreateIncomeRequest;
import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeResponse;

public interface CreateIncomeUseCase {
    IncomeResponse createIncome(Long userId, CreateIncomeRequest request);
}
