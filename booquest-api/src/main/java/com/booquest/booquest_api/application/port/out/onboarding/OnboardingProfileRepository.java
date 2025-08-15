package com.booquest.booquest_api.application.port.out.onboarding;

import com.booquest.booquest_api.domain.onboarding.model.OnboardingProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingProfileRepository extends JpaRepository<OnboardingProfile, Long> {
    boolean existsByUserId(Long userId);

    Optional<OnboardingProfile> findByUserId(Long userId);
}
