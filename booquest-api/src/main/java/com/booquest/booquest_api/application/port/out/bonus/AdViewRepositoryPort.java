package com.booquest.booquest_api.application.port.out.bonus;

import com.booquest.booquest_api.domain.bonus.model.AdView;

public interface AdViewRepositoryPort {
    AdView save(AdView adView);
    boolean existsCompletedByUserIdAndStepId(Long userId, Long stepId);
    long deleteByUserId(Long userId);
    /**
     * 특정 부퀘스트에 대한 완료된 광고 시청이 있는지 확인
     */
    boolean existsByStepIdAndUserIdAndIsCompletedTrue(Long stepId, Long userId);
}