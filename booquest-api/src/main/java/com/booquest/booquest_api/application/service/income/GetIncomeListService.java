package com.booquest.booquest_api.application.service.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeListResponse;
import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeResponse;
import com.booquest.booquest_api.application.port.in.income.GetIncomeListUseCase;
import com.booquest.booquest_api.application.port.out.usersidejob.UserSideJobRepositoryPort;
import com.booquest.booquest_api.domain.income.model.Income;
import com.booquest.booquest_api.adapter.out.income.persistence.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetIncomeListService implements GetIncomeListUseCase {

    private final IncomeRepository incomeRepository;
    private final UserSideJobRepositoryPort userSideJobRepositoryPort;

    @Override
    public IncomeListResponse getIncomeList(Long userId, Long userSideJobId) {
        if(userSideJobId != null) {
            checkUserSideJobExistsAndUserPermission(userSideJobId, userId);
        }

        List<Income> incomes = getIncomes(userId, userSideJobId);
        Totals totals = getTotals(userId, userSideJobId);

        List<IncomeResponse> mappedIncomes = mapToResponsesWithCumulativeAmount(incomes, totals.amount());
        return buildResponse(mappedIncomes, totals);
    }

    private void checkUserSideJobExistsAndUserPermission(Long userSideJobId, Long userId) {
        boolean exists = userSideJobRepositoryPort.existsByIdAndUserId(userSideJobId, userId);
        if (!exists) {
            throw new jakarta.persistence.EntityNotFoundException("UserSideJob not found: " + userSideJobId);
        }
    }

    private List<Income> getIncomes(Long userId, Long userSideJobId) {
        if(userSideJobId != null) {
            return incomeRepository.findByUserIdAndUserSideJobIdOrderByIncomeDateDesc(userId, userSideJobId);
        }
        return incomeRepository.findByUserIdOrderByIncomeDateDesc(userId);
    }

    private Totals getTotals(Long userId, Long userSideJobId) {
        if (userSideJobId != null) {
            Long amount = defaultZero(incomeRepository.sumAmountByUserIdAndUserSideJobId(userId, userSideJobId));
            Long count  = defaultZero(incomeRepository.countByUserIdAndUserSideJobId(userId, userSideJobId));
            return new Totals(amount, count);
        }
        Long amount = defaultZero(incomeRepository.sumAmountByUserId(userId));
        Long count  = defaultZero(incomeRepository.countByUserId(userId));
        return new Totals(amount, count);
    }

    private static Long defaultZero(Long v) { return v != null ? v : 0L; }
    private record Totals(Long amount, Long count) {}

    private List<IncomeResponse> mapToResponsesWithCumulativeAmount(List<Income> incomes, long totalAmount) {
        long running = totalAmount;
        List<IncomeResponse> result = new ArrayList<>(incomes.size());
        for (Income income : incomes) {
            result.add(IncomeResponse.toResponseWithCumulativeAmount(income, running));
            int amt = income.getAmount() == null ? 0 : income.getAmount();
            running -= amt;
        }
        return result;
    }

    private IncomeListResponse buildResponse(List<IncomeResponse> incomes, Totals totals) {
        return IncomeListResponse.builder()
                .incomes(incomes)
                .totalCount(totals.count())
                .totalAmount(totals.amount())
                .build();
    }
}
