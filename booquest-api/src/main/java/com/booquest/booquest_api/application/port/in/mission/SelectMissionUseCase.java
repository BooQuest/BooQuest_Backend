package com.booquest.booquest_api.application.port.in.mission;


import com.booquest.booquest_api.domain.mission.model.Mission;
import java.util.List;

public interface SelectMissionUseCase {
    Mission selectMission(Long missionId);

    List<Mission> selectMissionBySideJobId(Long sideJobId);

    int selectOrderNoByMissionId(Long missionId);
}
