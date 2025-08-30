package com.booquest.booquest_api.adapter.out.bonus.persistence;

import com.booquest.booquest_api.domain.bonus.model.Proof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProofJpaRepository extends JpaRepository<Proof, Long> {
    Optional<Proof> findByUserIdAndStepId(Long userId, Long stepId);
    boolean existsByUserIdAndStepId(Long userId, Long stepId);
    long deleteByUserId(Long userId);
    
    /**
     * 특정 부퀘스트에 대한 검증된 인증이 있는지 확인
     */
    boolean existsByStepIdAndUserIdAndIsVerifiedTrue(Long stepId, Long userId);
}