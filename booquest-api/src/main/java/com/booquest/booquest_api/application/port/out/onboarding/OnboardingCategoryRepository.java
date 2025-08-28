package com.booquest.booquest_api.application.port.out.onboarding;

import com.booquest.booquest_api.domain.onboarding.model.OnboardingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OnboardingCategoryRepository extends JpaRepository<OnboardingCategory, Long> {
    void deleteByProfileId(Long profileId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from OnboardingCategory c
         where c.profileId in (
            select p.id from OnboardingProfile p where p.userId = :userId
         )
    """)
    int deleteByUserId(@Param("userId") Long userId);
}
