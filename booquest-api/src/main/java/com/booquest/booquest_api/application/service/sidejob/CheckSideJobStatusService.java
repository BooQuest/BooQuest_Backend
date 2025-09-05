package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.application.port.in.onboarding.CheckSideJobStatusUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckSideJobStatusService implements CheckSideJobStatusUseCase {

    private final SideJobRepositoryPort sideJobRepositoryPort;
    private final MissionRepositoryPort missionRepositoryPort;

    @Override
    public boolean isSideJobRecommended(Long userId) {
        return sideJobRepositoryPort.isExistByUserId(userId);
    }

    @Override
    public boolean isMissionRecommended(Long userId) {
        return missionRepositoryPort.isExistByUserId(userId);
    }

    @Override
    public boolean isSideJobCreated(Long userId) {
        return missionRepositoryPort.existsByUserIdAndStatusNot(
                userId, MissionStatus.PLANNED
        );
    }

    @Override
    public Long getSelectedSideJobId(Long userId) {
        return sideJobRepositoryPort.findSelectedSideJobIdByUserId(userId);
    }
}
