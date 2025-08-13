package com.booquest.booquest_api.application.service.onboarding;

import com.booquest.booquest_api.application.port.onboarding.SubmitOnboardingUseCase;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingProfileRepository;
import com.booquest.booquest_api.application.port.out.user.UserRepository;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingProfile;
import com.booquest.booquest_api.domain.user.model.User;
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
    private final UserRepository userRepository;


    //온보딩 데이터는 하나만 허용
    @Transactional
    @Override
    public void submit(String providerUserId, String job, List<String> hobbies) {
        Long userId = userRepository.findUserIdByProviderUserId(providerUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (onboardingProfileRepository.existsById(userId)) {
            throw new IllegalStateException("이미 온보딩 정보가 존재합니다.");
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("job", job);
        metadata.put("hobbies", hobbies);

        OnboardingProfile profile = OnboardingProfile.builder()
                .userId(userId)
                .metadata(metadata)
                .build();

        onboardingProfileRepository.save(profile);
    }
}
