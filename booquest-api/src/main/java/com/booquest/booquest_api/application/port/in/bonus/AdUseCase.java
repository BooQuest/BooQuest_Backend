package com.booquest.booquest_api.application.port.in.bonus;

import com.booquest.booquest_api.adapter.in.bonus.dto.AdRequest;
import com.booquest.booquest_api.adapter.in.bonus.dto.BonusResponse;

public interface AdUseCase {
    BonusResponse watchAdAndGrantExp(Long userId, Long stepId, AdRequest request);
}
