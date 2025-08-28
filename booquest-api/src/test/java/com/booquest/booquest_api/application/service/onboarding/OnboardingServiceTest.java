package com.booquest.booquest_api.application.service.onboarding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.booquest.booquest_api.application.port.in.dto.SubmitOnboardingData;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingCategoryRepository;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingProfileRepository;
import com.booquest.booquest_api.adapter.out.user.persistence.UserRepository;
import com.booquest.booquest_api.domain.onboarding.enums.ExpressionStyle;
import com.booquest.booquest_api.domain.onboarding.enums.StrengthType;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingProfile;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OnboardingServiceTest {

    @Mock
    OnboardingProfileRepository onboardingProfileRepository;

    @Mock
    OnboardingCategoryRepository onboardingCategoryRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    OnboardingService service;

    private final String providerUserId = "kakao-123";
    private final Long userId = 42L;

    @Test
    @DisplayName("회원의 최초 온보딩 시 정상적으로 프로필과 카테고리가 저장된다")
    void submitOnboardingSuccess() {
        // given
        given(userRepository.existsById(userId)).willReturn(true);
        given(onboardingProfileRepository.findByUserId(userId)).willReturn(Optional.empty());

        ArgumentCaptor<OnboardingProfile> profileCaptor = ArgumentCaptor.forClass(OnboardingProfile.class);

        SubmitOnboardingData onboardingDataRequest = new SubmitOnboardingData(
                userId, "개발자", List.of("경제", "노래"), "글", "창작하기");

        // when
        service.submit(onboardingDataRequest);

        // then
        verify(onboardingProfileRepository).save(profileCaptor.capture());
        OnboardingProfile saved = profileCaptor.getValue();

        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getJob()).isEqualTo("개발자");
        assertThat(saved.getExpressionStyle().getDisplayName()).isEqualTo("글");
        assertThat(saved.getStrengthType().getDisplayName()).isEqualTo("창작하기");

        verify(userRepository).existsById(userId);
        verify(onboardingProfileRepository).findByUserId(userId);
        verify(onboardingCategoryRepository).deleteByProfileId(saved.getId());
        verify(onboardingCategoryRepository).saveAll(anyList());

        verifyNoMoreInteractions(userRepository, onboardingProfileRepository, onboardingCategoryRepository);
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 예외 발생")
    void onboardingIfNotUserThrowsException() {
        // given
        given(userRepository.existsById(userId)).willReturn(false);

        SubmitOnboardingData request = new SubmitOnboardingData(
                userId, "디자이너", List.of("요리"), "그림", "일상 공유하기"
        );

        // when & then
        assertThatThrownBy(() -> service.submit(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");

        verify(userRepository).existsById(userId);
        verifyNoInteractions(onboardingProfileRepository, onboardingCategoryRepository);
    }

    @Test
    @DisplayName("기존 프로필이 있으면 업데이트하고 카테고리는 새로 저장한다")
    void updateExistingOnboardingProfileAndReplaceCategories() {
        // given
        given(userRepository.existsById(userId)).willReturn(true);

        OnboardingProfile existingProfile = OnboardingProfile.builder()
                .id(100L)
                .userId(userId)
                .job("기존 직업")
                .expressionStyle(ExpressionStyle.IMAGE)
                .strengthType(StrengthType.SHARE)
                .build();

        given(onboardingProfileRepository.findByUserId(userId))
                .willReturn(Optional.of(existingProfile));

        SubmitOnboardingData request = new SubmitOnboardingData(
                userId, "기획자", List.of("음악", "사진"), "글", "창작하기"
        );

        ArgumentCaptor<OnboardingProfile> profileCaptor = ArgumentCaptor.forClass(OnboardingProfile.class);

        // when
        service.submit(request);

        // then
        verify(onboardingProfileRepository).save(profileCaptor.capture());
        OnboardingProfile savedProfile = profileCaptor.getValue();

        assertThat(savedProfile.getId()).isEqualTo(existingProfile.getId());
        assertThat(savedProfile.getJob()).isEqualTo("기획자");
        assertThat(savedProfile.getExpressionStyle()).isEqualTo(ExpressionStyle.TEXT);
        assertThat(savedProfile.getStrengthType()).isEqualTo(StrengthType.CREATE);

        verify(onboardingCategoryRepository).deleteByProfileId(100L);
        verify(onboardingCategoryRepository).saveAll(anyList());

        verify(userRepository).existsById(userId);
        verify(onboardingProfileRepository).findByUserId(userId);
    }
}
