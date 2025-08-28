package com.booquest.booquest_api.adapter.out.stepprogress;

import com.booquest.booquest_api.application.port.out.stepprogress.StepProgressRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StepProgressRepositoryAdapter implements StepProgressRepositoryPort {
    private final StepProgressRepository stepProgressRepository;

    @Override
    public long deleteByUserId(Long userId) {
        return stepProgressRepository.deleteByUserId(userId);
    }
}
