package com.booquest.booquest_api.application.service.missionstep;

import com.booquest.booquest_api.application.port.in.missionstep.DeleteMissionStepUseCase;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteMissionStepService implements DeleteMissionStepUseCase {

    private final MissionStepRepositoryPort missionStepRepositoryPort;

    @Override
    @Transactional
    public void deleteAllByMissionId(Long missionId) {
        missionStepRepositoryPort.deleteAllByMissionId(missionId);
    }
}
