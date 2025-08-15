package com.booquest.booquest_api.domain.onboarding.model;

import com.booquest.booquest_api.common.entity.CreatedOnlyEntity;
import com.booquest.booquest_api.domain.onboarding.enums.CategoryType;
import com.booquest.booquest_api.domain.onboarding.enums.SubCategoryType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "onboarding_categories")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OnboardingCategory extends CreatedOnlyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id")
    private Long profileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private CategoryType category;

    @Column(name = "sub_category", nullable = false)
    private String subCategory;
}
