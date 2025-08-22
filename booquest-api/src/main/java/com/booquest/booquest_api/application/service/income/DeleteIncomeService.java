package com.booquest.booquest_api.application.service.income;

import com.booquest.booquest_api.application.port.in.income.DeleteIncomeUseCase;
import com.booquest.booquest_api.domain.income.model.Income;
import com.booquest.booquest_api.adapter.out.income.persistence.IncomeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteIncomeService implements DeleteIncomeUseCase {

    private final IncomeRepository incomeRepository;

    @Override
    public void deleteIncome(Long userId, Long incomeId) {
        Income income = incomeRepository.findByIdAndUserId(incomeId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Income not found: " + incomeId));

        incomeRepository.delete(income);
    }
}
