package com.booquest.booquest_api.application.service.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeResponse;
import com.booquest.booquest_api.adapter.out.income.persistence.IncomeRepository;
import com.booquest.booquest_api.application.port.in.income.GetIncomeUseCase;
import com.booquest.booquest_api.domain.income.model.Income;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetIncomeService implements GetIncomeUseCase {
    private final IncomeRepository incomeRepository;

    @Override
    public IncomeResponse getIncome(Long userId, Long incomeId) {
        Income income = incomeRepository.findByIdAndUserId(incomeId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Income not found: " + incomeId));

        return IncomeResponse.toResponse(income);
    }
}
