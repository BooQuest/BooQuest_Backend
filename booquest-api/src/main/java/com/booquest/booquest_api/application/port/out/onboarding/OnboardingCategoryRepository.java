package com.booquest.booquest_api.application.port.out.onboarding;

import com.booquest.booquest_api.domain.onboarding.model.OnboardingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingCategoryRepository extends JpaRepository<OnboardingCategory, Long> {
    void deleteByProfileId(Long profileId);
}
