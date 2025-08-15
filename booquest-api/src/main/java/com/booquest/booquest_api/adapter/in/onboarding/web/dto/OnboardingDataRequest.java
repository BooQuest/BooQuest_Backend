package com.booquest.booquest_api.adapter.in.onboarding.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OnboardingDataRequest(
        long userId,
        @NotBlank String job,
        @NotNull @Size(min = 1) List<@NotBlank String> hobbies,
        String desiredSideJob) {
}
