package com.booquest.booquest_api.adapter.in.income.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateIncomeRequest {
    @NotBlank(message = "수익 내용은 필수입니다")
    private String title;

    @NotNull(message = "수익금은 필수입니다")
    @Min(value = 1, message = "수익금은 1원 이상이어야 합니다")
    private Integer amount;

    @NotNull(message = "수익 일자는 필수입니다")
    private LocalDate incomeDate;

    private String memo;
}
