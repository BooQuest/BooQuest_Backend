package com.booquest.booquest_api.adapter.in.onboarding.web.dto;

import com.booquest.booquest_api.domain.onboarding.enums.ExpressionStyle;
import com.booquest.booquest_api.domain.onboarding.enums.StrengthType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OnboardingDataRequest(
        long userId,
        @NotBlank String nickname,
        @NotBlank String job,
        @NotNull @Size(min = 1) List<@NotBlank String> hobbies, //subcategory
        @NotBlank String expressionStyle,
        @NotBlank String strengthType,
        @NotBlank String characterType) {
}
