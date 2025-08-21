package com.booquest.booquest_api.application.service.sidejob.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionProgressResponseDto;
import com.booquest.booquest_api.application.port.in.mission.GetMissionProgressUseCase;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.mission.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.mission.model.MissionStep;
import com.booquest.booquest_api.domain.sidejob.enums.MissionStatus;
import com.booquest.booquest_api.domain.sidejob.enums.StepStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMissionProgressService implements GetMissionProgressUseCase {
    
    private final MissionRepositoryPort missionRepository;
    private final MissionStepRepositoryPort missionStepRepository;
    
    @Override
    public MissionProgressResponseDto getMissionProgress(Long userId, Long sideJobId) {
        List<Mission> missions = missionRepository.findByUserIdAndSideJobIdOrderByOrderNo(userId, sideJobId);
        
        if (missions.isEmpty()) {
            return MissionProgressResponseDto.builder()
                    .currentMissionId(null)
                    .currentMissionOrder(null)
                    .currentMissionTitle(null)
                    .missionStepProgressPercentage(0.0)
                    .build();
        }

        Mission currentMission = findCurrentMission(missions);
        
        if (currentMission == null) {
            return MissionProgressResponseDto.builder()
                    .currentMissionId(null)
                    .currentMissionOrder(null)
                    .currentMissionTitle(null)
                    .missionStepProgressPercentage(0.0)
                    .build();
        }

        List<MissionStep> missionSteps = missionStepRepository.findByMissionIdOrderBySeq(currentMission.getId());

        double progressPercentage = calculateMissionStepProgress(missionSteps);
        
        return MissionProgressResponseDto.builder()
                .currentMissionId(currentMission.getId())
                .currentMissionOrder(currentMission.getOrderNo())
                .currentMissionTitle(currentMission.getTitle())
                .missionStepProgressPercentage(progressPercentage)
                .build();
    }
    
    private Mission findCurrentMission(List<Mission> missions) {
        Mission inProgressMission = missions.stream()
                .filter(mission -> mission.getStatus() == MissionStatus.IN_PROGRESS)
                .findFirst()
                .orElse(null);
        
        if (inProgressMission != null) {
            return inProgressMission;
        }

        return missions.stream()
                .filter(mission -> mission.getStatus() == MissionStatus.PLANNED)
                .findFirst()
                .orElse(null);
    }
    
    private double calculateMissionStepProgress(List<MissionStep> missionSteps) {
        if (missionSteps.isEmpty()) {
            return 0.0;
        }
        
        long completedSteps = missionSteps.stream()
                .filter(step -> step.getStatus() == StepStatus.COMPLETED)
                .count();
        
        return (double) completedSteps / missionSteps.size() * 100.0;
    }
} 