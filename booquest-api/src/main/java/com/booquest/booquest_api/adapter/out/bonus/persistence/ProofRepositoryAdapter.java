package com.booquest.booquest_api.adapter.out.bonus.persistence;

import com.booquest.booquest_api.application.port.out.bonus.ProofRepositoryPort;
import com.booquest.booquest_api.domain.bonus.model.Proof;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProofRepositoryAdapter implements ProofRepositoryPort {
    private final ProofJpaRepository proofJpaRepository;

    @Override
    public Proof save(Proof proof) {
        return proofJpaRepository.save(proof);
    }

    @Override
    public Optional<Proof> findByUserIdAndStepId(Long userId, Long stepId) {
        return proofJpaRepository.findByUserIdAndStepId(userId, stepId);
    }

    @Override
    public boolean existsByUserIdAndStepId(Long userId, Long stepId) {
        return proofJpaRepository.existsByUserIdAndStepId(userId, stepId);
    }

    @Override
    public long deleteByUserId(Long userId) {
        return proofJpaRepository.deleteByUserId(userId);
    }

    @Override
    public boolean existsByStepIdAndUserIdAndIsVerifiedTrue(Long stepId, Long userId) {
        return proofJpaRepository.existsByStepIdAndUserIdAndIsVerifiedTrue(stepId, userId);
    }
}