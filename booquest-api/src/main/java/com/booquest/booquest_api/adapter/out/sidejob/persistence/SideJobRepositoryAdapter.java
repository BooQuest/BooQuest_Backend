package com.booquest.booquest_api.adapter.out.sidejob.persistence;

import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class SideJobRepositoryAdapter implements SideJobRepositoryPort {

    private final SideJobRepository sideJobRepository;

    @Override
    public SideJob save(SideJob sideJob) {
        return sideJobRepository.save(sideJob);
    }

    @Override
    public int updateSelectedTrue(Long id) {
        return sideJobRepository.updateSelectedTrue(id);
    }

    @Override
    public Optional<SideJob> findByIdWithMissionsAndSteps(Long id) {
        return sideJobRepository.findByIdWithMissionsAndSteps(id);
    }

    @Override
    public boolean isExistByUserId(Long userId) {
        return sideJobRepository.existsByUserId(userId);
    }
}
