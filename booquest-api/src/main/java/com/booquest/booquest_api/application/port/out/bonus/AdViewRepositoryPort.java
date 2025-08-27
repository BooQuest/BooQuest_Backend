package com.booquest.booquest_api.application.port.out.bonus;

import com.booquest.booquest_api.domain.bonus.model.AdView;

public interface AdViewRepositoryPort {
    AdView save(AdView adView);
    boolean existsCompletedByUserIdAndStepId(Long userId, Long stepId);
}