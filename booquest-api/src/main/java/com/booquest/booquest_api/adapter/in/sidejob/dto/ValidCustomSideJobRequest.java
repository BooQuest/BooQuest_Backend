package com.booquest.booquest_api.adapter.in.sidejob.dto;

import jakarta.validation.constraints.NotEmpty;

public record ValidCustomSideJobRequest(@NotEmpty String sideJob) {

}
