package com.booquest.booquest_api.domain.bonus.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StepBonus {
    private Long id;
    private Long userId;
    private Long stepId;
    private int multiplier;
}
