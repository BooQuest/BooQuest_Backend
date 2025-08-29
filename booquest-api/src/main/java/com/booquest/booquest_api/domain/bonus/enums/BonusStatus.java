package com.booquest.booquest_api.domain.bonus.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


@JsonSerialize(using = ToStringSerializer.class)
public enum BonusStatus {
    @JsonProperty("granted")           GRANTED,
    @JsonProperty("not-completed")     NOT_COMPLETED,
    @JsonProperty("blocked-by-ad")     BLOCKED_BY_AD,
    @JsonProperty("blocked-by-proof")  BLOCKED_BY_PROOF,
    @JsonProperty("already-verified")  ALREADY_VERIFIED,
    @JsonProperty("already-watched")   ALREADY_WATCHED
}
