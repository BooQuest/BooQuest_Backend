package com.booquest.booquest_api.domain.missionstep.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonSerialize(using = ToStringSerializer.class)
public enum StepStatus {
    PLANNED, IN_PROGRESS, COMPLETED, SKIPPED
}
