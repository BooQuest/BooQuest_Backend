package com.booquest.booquest_api.adapter.out.income.persistence;

import com.booquest.booquest_api.application.port.out.income.IncomeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class IncomeRepositoryAdapter implements IncomeRepositoryPort {
    private final IncomeRepository incomeRepository;

    @Override
    public BigDecimal sumByUserAndSideJob(Long userId, Long userSideJobId) {
        Long sum = incomeRepository.sumAmountByUserAndSideJob(userId, userSideJobId); // amount는 Integer라 Long 합계가 옴
        return BigDecimal.valueOf(sum == null ? 0L : sum);
    }

    @Override
    public LocalDate findFirstIncomeDate(Long userId, Long userSideJobId) {
        return incomeRepository.findFirstIncomeDate(userId, userSideJobId);
    }

    @Override
    public long deleteByUserId(Long userId) {
        return incomeRepository.deleteByUserId(userId);
    }
}
