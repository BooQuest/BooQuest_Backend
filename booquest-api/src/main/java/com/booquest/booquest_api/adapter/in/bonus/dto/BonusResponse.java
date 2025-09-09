package com.booquest.booquest_api.adapter.in.bonus.dto;

import com.booquest.booquest_api.domain.bonus.enums.BonusStatus;

public record BonusResponse(
        BonusStatus status,
        int additionalExp,
        int totalStepExp,
        boolean leveledUp,
        int currentLevel
) {}
