package com.booquest.booquest_api.application.service.onboarding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.booquest.booquest_api.application.port.out.onboarding.OnboardingProfileRepository;
import com.booquest.booquest_api.application.port.out.user.UserRepository;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingProfile;
import java.util.List;
import java.util.Map;
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
    UserRepository userRepository;

    @InjectMocks
    OnboardingService service;

    private final String providerUserId = "kakao-123";
    private final Long userId = 42L;

    @Test
    @DisplayName("회원의 최초 온보딤만 정상적으로 데이터가 저장된다")
    void submitOnboardingSuccess() {
        // given
        given(userRepository.findUserIdByProviderUserId(providerUserId))
                .willReturn(Optional.of(userId));
        given(onboardingProfileRepository.existsByUserId(userId))
                .willReturn(false);

        ArgumentCaptor<OnboardingProfile> captor = ArgumentCaptor.forClass(OnboardingProfile.class);

        // when
        service.submit(providerUserId, "개발자", List.of("독서", "축구"));

        // then
        verify(onboardingProfileRepository).save(captor.capture());
        OnboardingProfile saved = captor.getValue();

        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getMetadata()).isInstanceOf(Map.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> meta = saved.getMetadata();
        assertThat(meta)
                .containsEntry("job", "개발자")
                .containsEntry("hobbies", List.of("독서", "축구"));

        verify(userRepository, times(1)).findUserIdByProviderUserId(providerUserId);
        verify(onboardingProfileRepository, times(1)).existsByUserId(userId);
        verifyNoMoreInteractions(userRepository, onboardingProfileRepository);
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 예외가 발생한다.")
    void onboardingIfNotUserThrowException() {
        // given
        given(userRepository.findUserIdByProviderUserId(providerUserId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                service.submit(providerUserId, "디자이너", List.of("요리"))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");

        verify(userRepository, times(1)).findUserIdByProviderUserId(providerUserId);
        verifyNoInteractions(onboardingProfileRepository); // 저장 시도 안 함
    }

    @Test
    @DisplayName("온보딩을 이미 진행한 회원은 예외가 발생하고 데이터를 저장하지 않는다.")
    void alreadyOnboardedUserThrowException() {
        // given
        given(userRepository.findUserIdByProviderUserId(providerUserId))
                .willReturn(Optional.of(userId));
        given(onboardingProfileRepository.existsByUserId(userId))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() ->
                service.submit(providerUserId, "마케터", List.of("등산"))
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 온보딩 정보가 존재합니다.");

        verify(onboardingProfileRepository, never()).save(any());
    }
}
