package com.booquest.booquest_api.domain.mission.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonSerialize(using = ToStringSerializer.class)
public enum MissionStatus {
    PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
}
