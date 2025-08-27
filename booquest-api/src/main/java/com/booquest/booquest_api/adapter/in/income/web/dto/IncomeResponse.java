package com.booquest.booquest_api.adapter.in.income.web.dto;

import com.booquest.booquest_api.domain.income.model.Income;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class IncomeResponse {
    private Long id;
    private Long userSideJobId;
    private String title;
    private Integer amount;
    private LocalDate incomeDate;
    private String memo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long cumulativeAmount;

    public static IncomeResponse toResponse(Income income) {
        return IncomeResponse.builder()
                .id(income.getId())
                .userSideJobId(income.getUserSideJobId())
                .title(income.getTitle())
                .amount(income.getAmount())
                .incomeDate(income.getIncomeDate())
                .memo(income.getMemo())
                .build();
    }

    public static IncomeResponse toResponseWithCumulativeAmount(Income income, long cumulativeAmount) {
        return IncomeResponse.builder()
                .id(income.getId())
                .userSideJobId(income.getUserSideJobId())
                .title(income.getTitle())
                .amount(income.getAmount())
                .incomeDate(income.getIncomeDate())
                .memo(income.getMemo())
                .cumulativeAmount(cumulativeAmount)
                .build();
    }
} 