package com.booquest.booquest_api.application.service.onboarding;

import com.booquest.booquest_api.adapter.in.onboarding.web.dto.OnboardingDataRequest;
import com.booquest.booquest_api.application.port.in.onboarding.SubmitOnboardingUseCase;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingCategoryRepository;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingProfileRepository;
import com.booquest.booquest_api.application.port.out.user.UserRepository;
import com.booquest.booquest_api.domain.onboarding.enums.ExpressionStyle;
import com.booquest.booquest_api.domain.onboarding.enums.StrengthType;
import com.booquest.booquest_api.domain.onboarding.enums.SubCategoryType;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingCategory;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingProfile;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void submit(OnboardingDataRequest request) {
        if (!userRepository.existsById(request.userId())){
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        if (onboardingProfileRepository.existsByUserId(request.userId())) {
            throw new IllegalStateException("이미 온보딩 정보가 존재합니다.");
        }

        OnboardingProfile profile = buildOnboardingProfile(request);
        onboardingProfileRepository.save(profile);

        buildAndSaveOnboardingCategory(request, profile);
    }

    private void buildAndSaveOnboardingCategory(OnboardingDataRequest request, OnboardingProfile profile) {
        for (String hobby : request.hobbies()) {
            SubCategoryType subCategoryType = SubCategoryType.from(hobby);
            onboardingCategoryRepository.save(
                    OnboardingCategory.builder()
                            .profileId(profile.getId())
                            .category(subCategoryType.getParentCategory())
                            .subCategory(subCategoryType)
                            .build()
            );
        }
    }

    private OnboardingProfile buildOnboardingProfile(OnboardingDataRequest request) {
        return OnboardingProfile.builder()
                .userId(request.userId())
                .job(request.job())
                .expressionStyle(ExpressionStyle.from(request.expressionStyle()))
                .strengthType(StrengthType.from(request.strengthType()))
                .build();
    }
}
