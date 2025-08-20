package com.booquest.booquest_api.application.service.sidejob.mission;

import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionProgressResponseDto;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GetMissionProgressUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.sidejob.mission.missionstep.MissionStepRepositoryPort;
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
        // 사용자의 모든 미션을 order_no 순으로 조회
        List<Mission> missions = missionRepository.findByUserIdAndSideJobIdOrderByOrderNo(userId, sideJobId);
        
        if (missions.isEmpty()) {
            return MissionProgressResponseDto.builder()
                    .currentMissionId(null)
                    .currentMissionOrder(null)
                    .currentMissionTitle(null)
                    .missionStepProgressPercentage(0.0)
                    .build();
        }
        
        // 현재 진행중인 미션 찾기 (IN_PROGRESS 상태이거나, PLANNED 상태 중 가장 낮은 order_no)
        Mission currentMission = findCurrentMission(missions);
        
        if (currentMission == null) {
            return MissionProgressResponseDto.builder()
                    .currentMissionId(null)
                    .currentMissionOrder(null)
                    .currentMissionTitle(null)
                    .missionStepProgressPercentage(0.0)
                    .build();
        }
        
        // 현재 미션의 부퀘스트들 조회
        List<MissionStep> missionSteps = missionStepRepository.findByMissionIdOrderBySeq(currentMission.getId());
        
        // 부퀘스트 진행률 계산
        double progressPercentage = calculateMissionStepProgress(missionSteps);
        
        return MissionProgressResponseDto.builder()
                .currentMissionId(currentMission.getId())
                .currentMissionOrder(currentMission.getOrderNo())
                .currentMissionTitle(currentMission.getTitle())
                .missionStepProgressPercentage(progressPercentage)
                .build();
    }
    
    private Mission findCurrentMission(List<Mission> missions) {
        // IN_PROGRESS 상태인 미션 찾기
        Mission inProgressMission = missions.stream()
                .filter(mission -> mission.getStatus() == MissionStatus.IN_PROGRESS)
                .findFirst()
                .orElse(null);
        
        if (inProgressMission != null) {
            return inProgressMission;
        }
        
        // IN_PROGRESS가 없으면 PLANNED 상태 중 가장 낮은 order_no 찾기
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