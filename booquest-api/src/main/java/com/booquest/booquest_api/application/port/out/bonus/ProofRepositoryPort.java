package com.booquest.booquest_api.application.port.out.bonus;

import com.booquest.booquest_api.domain.bonus.model.Proof;

import java.util.Optional;

public interface ProofRepositoryPort {
    Proof save(Proof proof);
    Optional<Proof> findByUserIdAndStepId(Long userId, Long stepId);
    boolean existsByUserIdAndStepId(Long userId, Long stepId);
    long deleteByUserId(Long userId);
    /**
     * 특정 부퀘스트에 대한 검증된 인증이 있는지 확인
     */
    boolean existsByStepIdAndUserIdAndIsVerifiedTrue(Long stepId, Long userId);
}