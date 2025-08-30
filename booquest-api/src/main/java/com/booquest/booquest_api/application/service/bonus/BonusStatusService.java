package com.booquest.booquest_api.application.service.bonus;

import com.booquest.booquest_api.application.port.out.bonus.BonusStatusPort;
import com.booquest.booquest_api.application.port.out.bonus.AdViewRepositoryPort;
import com.booquest.booquest_api.application.port.out.bonus.ProofRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BonusStatusService implements BonusStatusPort {
    private final AdViewRepositoryPort adViewRepositoryPort;
    private final ProofRepositoryPort proofRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public boolean hasAdBonus(Long stepId, Long userId) {
        return adViewRepositoryPort.existsByStepIdAndUserIdAndIsCompletedTrue(stepId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasProofBonus(Long stepId, Long userId) {
        return proofRepositoryPort.existsByStepIdAndUserIdAndIsVerifiedTrue(stepId, userId);
    }
} 