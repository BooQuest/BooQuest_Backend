package com.booquest.booquest_api.domain.mission.policy;

import com.booquest.booquest_api.domain.mission.model.Mission;

public interface MissionExpPolicy {
    int totalExpFor(Mission mission);
}
