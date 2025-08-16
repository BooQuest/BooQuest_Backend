package com.booquest.booquest_api.application.service.onboarding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.booquest.booquest_api.application.port.in.dto.SubmitOnboardingData;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingCategoryRepository;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingProfileRepository;
import com.booquest.booquest_api.application.port.out.user.UserRepository;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingProfile;
import java.util.List;
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
    @DisplayName("회원의 최초 온보딤만 정상적으로 데이터가 저장된다")
    void submitOnboardingSuccess() {
        // given
        given(userRepository.existsById(userId))
                .willReturn(true);
        given(onboardingProfileRepository.existsByUserId(userId))
                .willReturn(false);

        ArgumentCaptor<OnboardingProfile> captor = ArgumentCaptor.forClass(OnboardingProfile.class);

        SubmitOnboardingData onboardingDataRequest = new SubmitOnboardingData(
                userId, "개발자", List.of("경제", "노래"), "글", "창작하기");

        // when
        service.submit(onboardingDataRequest);

        // then
        verify(onboardingProfileRepository).save(captor.capture());
        OnboardingProfile saved = captor.getValue();


        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getJob()).isEqualTo("개발자");
        assertThat(saved.getExpressionStyle().getDisplayName()).isEqualTo("글");
        assertThat(saved.getStrengthType().getDisplayName()).isEqualTo("창작하기");

        verify(userRepository, times(1)).existsById(userId);
        verify(onboardingProfileRepository, times(1)).existsByUserId(userId);
        verifyNoMoreInteractions(userRepository, onboardingProfileRepository);
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
    @DisplayName("온보딩을 이미 진행한 회원은 예외 발생")
    void alreadyOnboardedUserThrowsException() {
        // given
        given(userRepository.existsById(userId)).willReturn(true);
        given(onboardingProfileRepository.existsByUserId(userId)).willReturn(true);

        SubmitOnboardingData request = new SubmitOnboardingData(
                userId, "마케터", List.of("등산"), "글", "트렌드 파악하기"
        );

        // when & then
        assertThatThrownBy(() -> service.submit(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 온보딩 정보가 존재합니다.");

        verify(userRepository).existsById(userId);
        verify(onboardingProfileRepository).existsByUserId(userId);
        verifyNoMoreInteractions(userRepository, onboardingProfileRepository);
        verifyNoInteractions(onboardingCategoryRepository);
    }
}
