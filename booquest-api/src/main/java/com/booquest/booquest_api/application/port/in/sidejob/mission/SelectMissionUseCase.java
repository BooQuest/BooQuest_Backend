package com.booquest.booquest_api.application.port.in.sidejob.mission;


import com.booquest.booquest_api.domain.mission.model.Mission;

public interface SelectMissionUseCase {
    Mission selectMission(Long missionId);
}
