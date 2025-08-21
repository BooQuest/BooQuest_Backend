package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.application.port.in.sidejob.SelectSideJobUseCase;
import com.booquest.booquest_api.application.port.in.mission.SelectMissionUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SelectSideJobService implements SelectSideJobUseCase {
    private final SideJobRepositoryPort sideJobRepository;

    @Override
    public SideJob selectSideJob(Long sideJobId) {
        return sideJobRepository.findByIdWithMissionsAndSteps(sideJobId)
                .orElseThrow(() -> new EntityNotFoundException("SideJob not found: " + sideJobId));
    }

    @Override
    public List<SideJob> selectSideJobsByUserId(Long userId) {
        return sideJobRepository.findAllByUserId(userId);
    }


}
