package com.booquest.booquest_api.domain.mission.policy;

import com.booquest.booquest_api.domain.mission.model.Mission;
import org.springframework.stereotype.Component;

@Component
public class FixedMissionExpPolicy implements MissionExpPolicy {
    private static final int MISSION_TOTAL_EXP = 50;

    @Override
    public int totalExpFor(Mission mission) {
        return MISSION_TOTAL_EXP;
    }
}
