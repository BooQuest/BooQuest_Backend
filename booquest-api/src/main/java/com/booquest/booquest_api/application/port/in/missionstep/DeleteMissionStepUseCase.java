package com.booquest.booquest_api.application.port.in.missionstep;

public interface DeleteMissionStepUseCase {
    void deleteAllByMissionId(Long missionId);
}
