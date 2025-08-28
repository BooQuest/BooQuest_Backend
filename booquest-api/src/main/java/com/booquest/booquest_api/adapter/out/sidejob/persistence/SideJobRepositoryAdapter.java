package com.booquest.booquest_api.adapter.out.sidejob.persistence;

import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


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

    @Override
    public List<SideJob> findAllByIds(List<Long> sideJobIds) {
        return sideJobRepository.findAllById(sideJobIds);
    }

    @Override
    public List<SideJob> findAllByUserId(Long userId) {
        return sideJobRepository.findAllByUserId(userId);
    }

    @Override
    public List<SideJob> findTop3ByUserIdOrderByCreatedAtDesc(Long userId) {
        return sideJobRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Long findSelectedSideJobByUserId(Long userId) {
        return sideJobRepository.findTopSelectedSideJobId(userId)
                .orElse(null);
    }

    @Override
    public long deleteByUserId(Long userId) {
        return sideJobRepository.deleteByUserId(userId);
    }
}
