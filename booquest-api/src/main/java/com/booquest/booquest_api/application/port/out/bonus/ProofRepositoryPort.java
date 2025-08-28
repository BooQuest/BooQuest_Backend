package com.booquest.booquest_api.application.port.out.bonus;

import com.booquest.booquest_api.domain.bonus.model.Proof;

import java.util.Optional;

public interface ProofRepositoryPort {
    Proof save(Proof proof);
    Optional<Proof> findByUserIdAndStepId(Long userId, Long stepId);
    boolean existsByUserIdAndStepId(Long userId, Long stepId);
    long deleteByUserId(Long userId);
}