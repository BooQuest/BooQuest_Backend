package com.booquest.booquest_api.application.port.in.income;

public interface DeleteIncomeUseCase {
    void deleteIncome(Long userId, Long incomeId);
}
