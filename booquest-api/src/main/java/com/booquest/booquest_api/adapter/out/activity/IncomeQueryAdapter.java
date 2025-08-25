package com.booquest.booquest_api.adapter.out.activity;

import com.booquest.booquest_api.adapter.out.income.persistence.IncomeRepository;
import com.booquest.booquest_api.application.port.out.activity.IncomeQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncomeQueryAdapter implements IncomeQueryPort {
    private final IncomeRepository incomeRepository;

    @Override
    public Long sumIncomeByUserId(Long userId) {
        return incomeRepository.sumAmountByUserId(userId);
    }
}
