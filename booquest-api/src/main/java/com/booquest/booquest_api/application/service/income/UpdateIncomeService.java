package com.booquest.booquest_api.application.service.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.UpdateIncomeRequest;
import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeResponse;
import com.booquest.booquest_api.application.port.in.income.UpdateIncomeUseCase;
import com.booquest.booquest_api.domain.income.model.Income;
import com.booquest.booquest_api.adapter.out.income.persistence.IncomeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateIncomeService implements UpdateIncomeUseCase {

    private final IncomeRepository incomeRepository;

    @Override
    public IncomeResponse updateIncome(Long userId, Long incomeId, UpdateIncomeRequest request) {
        validateRequest(request);

        Income income = getIncomeAndCheckUserPermission(userId, incomeId);

        applyUpdates(income, request);
        Income updatedIncome = save(income);

        return buildResponse(updatedIncome);
    }

    private void validateRequest(UpdateIncomeRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        if (request.getAmount() == null || request.getAmount() < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }
        if (request.getIncomeDate() == null) {
            throw new IllegalArgumentException("incomeDate is required");
        }
    }

    private Income getIncomeAndCheckUserPermission(Long userId, Long incomeId) {
        return incomeRepository.findByIdAndUserId(incomeId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Income not found: " + incomeId));
    }

    private void applyUpdates(Income income, UpdateIncomeRequest request) {
        income.update(request.getTitle(), request.getAmount(), request.getIncomeDate(), request.getMemo());
    }

    private Income save(Income income) {
        return incomeRepository.save(income);
    }

    private IncomeResponse buildResponse(Income income) {
        return IncomeResponse.builder()
                .id(income.getId())
                .userSideJobId(income.getUserSideJobId())
                .title(income.getTitle())
                .amount(income.getAmount())
                .incomeDate(income.getIncomeDate())
                .memo(income.getMemo())
                .build();
    }
}
