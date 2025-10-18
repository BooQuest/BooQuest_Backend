package com.booquest.booquest_api.application.service.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionResponseDto;
import com.booquest.booquest_api.application.port.in.mission.CreateMissionUseCase;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.mission.enums.MainQuest;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateMissionService implements CreateMissionUseCase {
    private final MissionRepositoryPort missionRepository;
    private final SideJobRepositoryPort sideJobRepository;

    @Override
    public List<MissionResponseDto> createMission(Long sideJobId, Long userId) {
        SideJob sideJob = sideJobRepository.findById(sideJobId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부업입니다."));

        List<Mission> missions = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Mission mission = Mission.builder()
                    .sideJob(sideJob)
                    .userId(userId)
                    .orderNo(i)
                    .title(MainQuest.getTitleByOrderNo(i))
                    .designNotes(MainQuest.getDesignNotesByOrderNo(i))
                    .status(MissionStatus.PLANNED)
                    .guide(MainQuest.getGuideByOrderNo(i))
                    .build();

            missions.add(mission);
        }

        missionRepository.saveAll(missions);

        return missions.stream()
                .map(MissionResponseDto::fromEntity)
                .toList();
    }
}
