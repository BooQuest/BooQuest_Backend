package com.booquest.booquest_api.adapter.out.bonus.persistence;

import com.booquest.booquest_api.application.port.out.bonus.AdViewRepositoryPort;
import com.booquest.booquest_api.domain.bonus.model.AdView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdViewRepositoryAdapter implements AdViewRepositoryPort {
    private final AdViewJpaRepository adViewJpaRepository;

    @Override
    public AdView save(AdView adView) {
        return adViewJpaRepository.save(adView);
    }

    @Override
    public boolean existsCompletedByUserIdAndStepId(Long userId, Long stepId) {
        return false;
    }

    @Override
    public long deleteByUserId(Long userId) {
        return adViewJpaRepository.deleteByUserId(userId);
    }
}