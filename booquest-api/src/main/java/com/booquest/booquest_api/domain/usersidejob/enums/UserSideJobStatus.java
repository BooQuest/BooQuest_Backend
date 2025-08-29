package com.booquest.booquest_api.domain.usersidejob.enums;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonSerialize(using = ToStringSerializer.class)
public enum UserSideJobStatus {
    PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
}
