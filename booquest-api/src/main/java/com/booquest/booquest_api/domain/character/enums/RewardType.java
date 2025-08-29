package com.booquest.booquest_api.domain.character.enums;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonSerialize(using = ToStringSerializer.class)
public enum RewardType {
    STEP_COMPLETED, STEP_MARKED_INCOMPLETE, PROOF_VERIFIED, AD_WATCHED, NONE
}
