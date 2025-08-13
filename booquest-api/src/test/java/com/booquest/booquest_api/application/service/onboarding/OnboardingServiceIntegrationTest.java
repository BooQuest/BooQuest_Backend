package com.booquest.booquest_api.application.service.onboarding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.booquest.booquest_api.application.port.out.onboarding.OnboardingProfileRepository;
import com.booquest.booquest_api.application.port.out.user.UserRepository;
import com.booquest.booquest_api.domain.onboarding.model.OnboardingProfile;
import com.booquest.booquest_api.domain.user.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Testcontainers
class OnboardingServiceIntegrationTest {

    // Testcontainers Postgres
    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    //컨테이너 값들을 Spring 환경 속성으로 주입
    @DynamicPropertySource
    static void registerDatasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",      POSTGRES::getJdbcUrl);
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

    @BeforeEach
    void setUpUser() {
        Optional<Long> maybeId = userRepository.findUserIdByProviderUserId(providerUserId);
        if (maybeId.isEmpty()) {
            User user = User.builder()
                    .provider("kakao")
                    .providerUserId(providerUserId)
                    .email("t@example.com")
                    .nickname("tester")
                    .build();

            userRepository.save(user);
        }
    }

    @Test
    @DisplayName("회원의 최초 온보딩만 정상적으로 저장된다.")
    void submitOnboardingSuccess() {
        // when
        onboardingService.submit(providerUserId, "개발자", List.of("독서", "축구"));

        // then
        Long userId = userRepository.findUserIdByProviderUserId(providerUserId).orElseThrow();
        OnboardingProfile saved = onboardingProfileRepository.findByUserId(userId).orElseThrow();

        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getMetadata()).isInstanceOf(Map.class);

        @SuppressWarnings("unchecked")
        Map<String, Object> meta = saved.getMetadata();
        assertThat(meta)
                .containsEntry("job", "개발자")
                .containsEntry("hobbies", List.of("독서", "축구"));
    }


    @Test
    @DisplayName("온보딩을 이미 진행한 회원은 예외가 발생하고 데이터를 저장하지 않는다.")
    void alreadyOnboardedUserThrowException() {
        onboardingService.submit(providerUserId, "개발자", List.of("독서"));
        onboardingProfileRepository.flush();

        assertThatThrownBy(() ->
                onboardingService.submit(providerUserId, "디자이너", List.of("요리"))
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 온보딩 정보가 존재합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 회원이면 예외가 발생한다.")
    void onboardingIfNotUserThrowException() {
        String unknown = "kakao-999";

        assertThatThrownBy(() ->
                onboardingService.submit(unknown, "개발자", List.of("독서"))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }
}
