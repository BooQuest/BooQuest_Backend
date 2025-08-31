package com.booquest.booquest_api.domain.mission.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MissionCompleteStatus {
    @JsonProperty("completed")                  COMPLETED,
    @JsonProperty("not-all-steps-completed")    NOT_ALL_STEPS_COMPLETED,
    @JsonProperty("not-in-progress")            NOT_IN_PROGRESS,
    @JsonProperty("already-completed")          ALREADY_COMPLETED,
    @JsonProperty("mission-not-found")          MISSION_NOT_FOUND
} 