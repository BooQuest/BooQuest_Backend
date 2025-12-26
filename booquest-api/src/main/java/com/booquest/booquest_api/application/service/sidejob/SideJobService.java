package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.application.port.in.sidejob.CreateSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.DeleteSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.SelectSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.UpdateSideJobUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SideJobService implements SelectSideJobUseCase, DeleteSideJobUseCase, UpdateSideJobUseCase,
        CreateSideJobUseCase {

    private final SideJobRepositoryPort sideJobRepository;

    @Override
    public SideJob selectSideJob(Long sideJobId) {
        return sideJobRepository.findByIdWithMissionsAndSteps(sideJobId)
                .orElseThrow(() -> new EntityNotFoundException("SideJob not found: " + sideJobId));
    }

    @Override
    public List<SideJob> selectSideJobsByUserId(Long userId) {
        return sideJobRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public SideJob getSelectedSideJobsByUserId(Long userId) {
        return sideJobRepository.findSelectedSideJobByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("SideJob not found"));
    }

    @Override
    @Transactional
    public void deleteAllSideJob(Long userId) {
        List<SideJob> sideJobs = sideJobRepository.findAllByUserId(userId);

        sideJobRepository.deleteAll(sideJobs);
    }

    @Override
    public void deleteSideJob(Long userId, Long sideJobId) {
        SideJob sideJob = sideJobRepository.findByIdAndUserId(sideJobId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 부업이 존재하지 않거나 권한이 없습니다."));

        sideJobRepository.delete(sideJob);
    }

    @Override
    @Transactional
    public void clearAllSelected(List<SideJob> sideJobs) {
        for (SideJob sideJob : sideJobs) {
            sideJob.clearIsSelected();
        }
    }

    @Override
    @Transactional
    public void markSelected(SideJob sideJob) {
        sideJob.markSelected();
    }

    @Override
    public SideJob create(String sideJobTitle, Long userId) {
        SideJob sideJob = SideJob.builder()
                .title(sideJobTitle)
                .userId(userId)
                .build();

        return sideJobRepository.save(sideJob);
    }
}
