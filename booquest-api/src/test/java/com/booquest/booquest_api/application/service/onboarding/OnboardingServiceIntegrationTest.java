package com.booquest.booquest_api.application.service.onboarding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.booquest.booquest_api.adapter.out.auth.persistence.jpa.UserRepository;
import com.booquest.booquest_api.application.port.in.dto.SubmitOnboardingData;
import com.booquest.booquest_api.application.port.out.onboarding.OnboardingProfileRepository;
import com.booquest.booquest_api.domain.auth.enums.AuthProvider;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingProfile;
import com.booquest.booquest_api.domain.user.model.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


/**
 * PostgreSql에 onboarding 데이터가 저장되는지 확인하기 위한 통합 테스트
 * 사전조건 : Docker가 돌아가고 있어야 TestContainer가 작동해서 테스트를 실행 할 수 있습니다
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Testcontainers
class OnboardingServiceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerDatasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.flyway.enabled", () -> true);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired
    OnboardingService onboardingService;
    @Autowired
    OnboardingProfileRepository onboardingProfileRepository;
    @Autowired
    UserRepository userRepository;

    private final String providerUserId = "kakao-123";
    private Long userId;

    @BeforeEach
    void setUpUser() {
        userId = userRepository.findIdByProviderAndProviderUserId(AuthProvider.KAKAO, providerUserId).orElseGet(() -> {
            User user = User.builder()
                    .provider(AuthProvider.KAKAO)
                    .providerUserId(providerUserId)
                    .email("t@example.com")
                    .nickname("tester")
                    .profileImageUrl("image")
                    .build();
            return userRepository.save(user).getId();
        });
    }

    @Test
    @DisplayName("회원의 최초 온보딩만 정상적으로 저장된다.")
    void submitOnboardingSuccess() {
        // given
        SubmitOnboardingData request = new SubmitOnboardingData(
                userId,
                "개발자",
                List.of("노래", "다이어트"),
                "글",
                "정리·전달하기"
        );

        // when
        onboardingService.submit(request);

        // then
        OnboardingProfile saved = onboardingProfileRepository.findByUserId(userId)
                .orElseThrow();

        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getJob()).isEqualTo("개발자");
        assertThat(saved.getExpressionStyle().getDisplayName()).isEqualTo("글");
        assertThat(saved.getStrengthType().getDisplayName()).isEqualTo("정리·전달하기");
    }

    //온보딩 인증 추가 후 주석 해제
//    @Test
//    @DisplayName("온보딩을 이미 진행한 회원은 예외가 발생하고 데이터를 저장하지 않는다.")
//    void alreadyOnboardedUserThrowException() {
//        SubmitOnboardingData request1 = new SubmitOnboardingData(
//                userId,
//                "개발자",
//                List.of("요리"),
//                "영상",
//                "창작하기"
//        );
//        onboardingService.submit(request1);
//
//        onboardingProfileRepository.flush();
//
//        SubmitOnboardingData request2 = new SubmitOnboardingData(
//                userId,
//                "마케터",
//                List.of("운동"),
//                "그림",
//                "트렌드 파악하기"
//        );
//
//        assertThatThrownBy(() -> onboardingService.submit(request2))
//                .isInstanceOf(IllegalStateException.class)
//                .hasMessage("이미 온보딩 정보가 존재합니다.");
//    }

    @Test
    @DisplayName("존재하지 않는 회원이면 예외가 발생한다.")
    void onboardingIfNotUserThrowException() {
        long unknownUserId = 99999L;

        SubmitOnboardingData request = new SubmitOnboardingData(
                unknownUserId,
                "디자이너",
                List.of("음악"),
                "영상",
                "일상 공유하기"
        );

        assertThatThrownBy(() -> onboardingService.submit(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }
}
