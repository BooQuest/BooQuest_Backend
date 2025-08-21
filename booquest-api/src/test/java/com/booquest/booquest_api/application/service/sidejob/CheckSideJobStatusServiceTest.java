package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("CheckSideJobStatusService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class CheckSideJobStatusServiceTest {

    @Mock
    private SideJobRepositoryPort sideJobRepositoryPort;
    @Mock
    private MissionRepositoryPort missionRepositoryPort;
    @InjectMocks
    private CheckSideJobStatusService service;

    @BeforeEach
    void setUp() {
        sideJobRepositoryPort = mock(SideJobRepositoryPort.class);
        missionRepositoryPort = mock(MissionRepositoryPort.class);
        service = new CheckSideJobStatusService(sideJobRepositoryPort, missionRepositoryPort);
    }

    @Test
    @DisplayName("사용자에게 추천된 부업이 존재할 경우 true 반환")
    void isSideJobRecommended_WhenExists_ReturnsTrue() {
        // given
        Long userId = 1L;
        given(sideJobRepositoryPort.isExistByUserId(userId)).willReturn(true);

        // when
        boolean result = service.isSideJobRecommended(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자에게 추천된 미션이 존재할 경우 true 반환")
    void isMissionRecommended_WhenExists_ReturnsTrue() {
        // given
        Long userId = 2L;
        given(missionRepositoryPort.isExistByUserId(userId)).willReturn(true);

        // when
        boolean result = service.isMissionRecommended(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자가 생성한 부업에 PLANNED가 아닌 미션이 존재할 경우 true 반환")
    void isSideJobCreated_WhenNonPlannedMissionExists_ReturnsTrue() {
        // given
        Long userId = 3L;
        when(missionRepositoryPort.existsByUserIdAndStatusNot(userId, MissionStatus.PLANNED))
                .thenReturn(true);

        // when
        boolean result = service.isSideJobCreated(userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자가 생성한 모든 미션이 PLANNED일 경우 false 반환")
    void isSideJobCreated_WhenOnlyPlannedMissionsExist_ReturnsFalse() {
        // given
        Long userId = 4L;
        given(missionRepositoryPort.existsByUserIdAndStatusNot(userId, MissionStatus.PLANNED))
                .willReturn(false);

        // when
        boolean result = service.isSideJobCreated(userId);

        // then
        assertThat(result).isFalse();
    }
}
