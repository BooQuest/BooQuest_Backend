package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.application.port.in.sidejob.UpdateSideJobUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UpdateSideJobService implements UpdateSideJobUseCase {
    private final SideJobRepositoryPort sideJobRepository;

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
}
