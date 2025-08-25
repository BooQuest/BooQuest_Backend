package com.booquest.booquest_api.application.port.out.income;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IncomeRepositoryPort {
    BigDecimal sumByUserAndSideJob(Long userId, Long userSideJobId);
    LocalDate findFirstIncomeDate(Long userId, Long userSideJobId);
}
