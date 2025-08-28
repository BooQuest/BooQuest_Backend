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
}