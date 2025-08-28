package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.application.port.in.sidejob.DeleteSideJobUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteSideJobService implements DeleteSideJobUseCase {

    private final SideJobRepositoryPort sideJobRepository;

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
}
