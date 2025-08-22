package com.booquest.booquest_api.adapter.in.income.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class IncomeListResponse {
    private List<IncomeResponse> incomes;
    private Long totalCount;
    private Long totalAmount;
}
