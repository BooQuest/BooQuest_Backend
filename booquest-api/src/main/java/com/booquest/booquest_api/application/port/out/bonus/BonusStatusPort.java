package com.booquest.booquest_api.application.port.out.bonus;

public interface BonusStatusPort {
    /**
     * 특정 부퀘스트에 대한 광고 시청 여부 확인
     */
    boolean hasAdBonus(Long stepId, Long userId);

    /**
     * 특정 부퀘스트에 대한 인증 완료 여부 확인
     */
    boolean hasProofBonus(Long stepId, Long userId);
} 