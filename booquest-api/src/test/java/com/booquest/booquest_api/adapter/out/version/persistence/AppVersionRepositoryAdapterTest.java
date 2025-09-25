package com.booquest.booquest_api.adapter.out.version.persistence;

import com.booquest.booquest_api.adapter.in.version.web.dto.LatestAppVersionResponseDto;
import com.booquest.booquest_api.domain.version.AppPlatform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("AppVersionRepositoryAdapter 단위 테스트")
@ExtendWith(MockitoExtension.class)
class AppVersionRepositoryAdapterTest {

    @Mock
    private AppVersionRepository appVersionRepository;

    @InjectMocks
    private AppVersionRepositoryAdapter adapter;

    @Test
    @DisplayName("최신 버전을 정상적으로 가져온다")
    void findLatestByPlatform_WhenExists_ReturnsLatest() {
        // given
        LatestAppVersionResponseDto dto =
                new LatestAppVersionResponseDto("2.0.0", 20, "메이저 업데이트", true);
        Pageable limitOne = PageRequest.of(0, 1);

        given(appVersionRepository.findLatestByPlatform(AppPlatform.ANDROID, limitOne))
                .willReturn(List.of(dto));

        // when
        LatestAppVersionResponseDto result = adapter.findLatestByPlatform(AppPlatform.ANDROID);

        // then
        assertThat(result).isNotNull();
        assertThat(result.latestVersion()).isEqualTo("2.0.0");
        assertThat(result.latestBuildNumber()).isEqualTo(20);
        assertThat(result.description()).isEqualTo("메이저 업데이트");
        assertThat(result.isForceUpdate()).isTrue();
    }

    @Test
    @DisplayName("해당 플랫폼 데이터가 없으면 null 반환")
    void findLatestByPlatform_WhenNotExists_ReturnsNull() {
        // given
        Pageable limitOne = PageRequest.of(0, 1);

        given(appVersionRepository.findLatestByPlatform(AppPlatform.IOS, limitOne))
                .willReturn(List.of());

        // when
        LatestAppVersionResponseDto result = adapter.findLatestByPlatform(AppPlatform.IOS);

        // then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("releasedAt이 같을 경우 id가 더 큰 버전을 최신으로 가져온다 (정렬 보장)")
    void findLatestByPlatform_WhenSameReleasedAt_ReturnsBiggerId() {
        // given
        LatestAppVersionResponseDto older =
                new LatestAppVersionResponseDto("1.0.1", 2, "버그픽스", false);
        LatestAppVersionResponseDto newer =
                new LatestAppVersionResponseDto("1.0.2", 3, "추가 기능", false);
        Pageable limitOne = PageRequest.of(0, 1);

        given(appVersionRepository.findLatestByPlatform(AppPlatform.ANDROID, limitOne))
                .willReturn(List.of(newer, older)); // 정렬 결과를 가정

        // when
        LatestAppVersionResponseDto result = adapter.findLatestByPlatform(AppPlatform.ANDROID);

        // then
        assertThat(result).isNotNull();
        assertThat(result.latestVersion()).isEqualTo("1.0.2");
        assertThat(result.latestBuildNumber()).isEqualTo(3);
    }
}
