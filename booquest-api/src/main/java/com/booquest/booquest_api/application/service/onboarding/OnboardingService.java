package com.booquest.booquest_api.application.service.onboarding;

import com.booquest.booquest_api.application.port.in.dto.SubmitOnboardingData;
import com.booquest.booquest_api.application.port.in.onboarding.SubmitOnboardingUseCase;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingCategoryRepository;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingProfileRepository;
import com.booquest.booquest_api.adapter.out.auth.persistence.jpa.UserRepository;
import com.booquest.booquest_api.domain.onboarding.enums.ExpressionStyle;
import com.booquest.booquest_api.domain.onboarding.enums.StrengthType;
import com.booquest.booquest_api.domain.onboarding.enums.SubCategoryType;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingCategory;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingProfile;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardingService implements SubmitOnboardingUseCase {

    private final OnboardingProfileRepository onboardingProfileRepository;
    private final OnboardingCategoryRepository onboardingCategoryRepository;
    private final UserRepository userRepository;


    //온보딩 데이터는 하나만 허용
    @Transactional
    @Override
    public void submit(SubmitOnboardingData onboardingData) {
        if (!userRepository.existsById(onboardingData.userId())){
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

//        if (onboardingProfileRepository.existsByUserId(onboardingData.userId())) {
//            throw new IllegalStateException("이미 온보딩 정보가 존재합니다.");
//        }

        OnboardingProfile profile = buildOnboardingProfile(onboardingData);
        onboardingProfileRepository.save(profile);

        List<OnboardingCategory> onboardingCategories = buildAndSaveOnboardingCategory(onboardingData.hobbies(), profile);
        onboardingCategoryRepository.saveAll(onboardingCategories);
    }

    private List<OnboardingCategory> buildAndSaveOnboardingCategory(List<String> hobbies, OnboardingProfile profile) {
        List<OnboardingCategory> onboardingCategories = new ArrayList<>();

        for (String hobby : hobbies) {
            OnboardingCategory onboardingCategory = OnboardingCategory.builder()
                    .profileId(profile.getId())
                    .category(SubCategoryType.from(hobby).getParentCategory())
                    .subCategory(hobby)
                    .build();

            onboardingCategories.add(onboardingCategory);
        }
        return onboardingCategories;
    }

    private OnboardingProfile buildOnboardingProfile(SubmitOnboardingData onboardingData) {
        return OnboardingProfile.builder()
                .userId(onboardingData.userId())
                .job(onboardingData.job())
                .expressionStyle(ExpressionStyle.from(onboardingData.expressionStyle()))
                .strengthType(StrengthType.from(onboardingData.strengthType()))
                .build();
    }
}
