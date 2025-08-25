package com.booquest.booquest_api.application.service.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.CurrentMissionProgress;
import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobProgressDto;
import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobProgressResponse;
import com.booquest.booquest_api.adapter.in.usersidejob.dto.StageProgress;
import com.booquest.booquest_api.adapter.out.mission.persisitence.MissionRepository;
import com.booquest.booquest_api.adapter.out.missionstep.persisitence.MissionStepRepository;
import com.booquest.booquest_api.application.port.in.usersidejob.GetUserSideJobProgressUseCase;
import com.booquest.booquest_api.application.port.out.usersidejob.UserSideJobRepositoryPort;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetUserSideJobProgressService implements GetUserSideJobProgressUseCase {
    private final UserSideJobRepositoryPort userSideJobRepositoryPort;
    private final MissionRepository missionRepository;
    private final MissionStepRepository missionStepRepository;

    @Override
    public UserSideJobProgressResponse getUserSideJobProgress(Long userId) {
        // 1. 최신 진행중 사용자 부업(UserSideJob) 1건 조회
        UserSideJob userSideJob = userSideJobRepositoryPort
                .findLatestSideJobForStatus(userId)
                .orElseThrow(() -> new EntityNotFoundException("No IN_PROGRESS side job for user: " + userId));

        Long sideJobId = userSideJob.getSideJobId();
        String title = userSideJob.getTitle();

        // 2. 메인퀘스트(미션)와 해당 부퀘스트(미션스텝)를 배치로 로드
        // - 미션 전체를 먼저 가져옴
        // - 미션 ID들로 스텝을 한 번에 조회해 미션별로 묶음
        List<Mission> missions = missionRepository.findMissionsBySideJobId(sideJobId); // orderNo ASC 가정
        List<Long> missionIds = missions.stream().map(Mission::getId).collect(Collectors.toList());
        List<MissionStep> allSteps = missionIds.isEmpty()
                ? List.of()
                : missionStepRepository.findStepsByMissionIds(missionIds);

        Map<Long, List<MissionStep>> stepsByMission =
                allSteps.stream().collect(Collectors.groupingBy(MissionStep::getMissionId));

        // 3. 부업 전체 진행률 계산
        // - 미션 균등 가중치: 각 미션 가중치 = 100 / M
        // - 미션 기여도 = (완료스텝 / 전체스텝)
        // - 스텝이 0개인 미션은 상태 COMPLETED면 1.0, 아니면 0.0
        int missionTotal = missions.size();
        double missionWeight = missionTotal == 0 ? 0.0 : 100.0 / missionTotal;

        double sum = 0.0;
        for (Mission m : missions) {
            var ms = stepsByMission.getOrDefault(m.getId(), List.of());
            int total = ms.size();
            long done = ms.stream().filter(MissionStep::isCompleted).count();

            double part = (total == 0) ? 0.0 : done / (double) total;
            sum += missionWeight * part;
        }
        int overallPercent = (int) Math.round(sum);

        // 4. 현재 진행 중 미션 & 완료된 미션 수 & 남은 단계 계산
        // - 현재 미션: 완료되지 않은 스텝이 하나라도 남아있는 첫 미션
        // - 완료 미션: 모든 스텝 완료(또는 스텝 0개이고 상태 COMPLETED)
        var currentOpt = missions.stream()
                .filter(m -> stepsByMission.getOrDefault(m.getId(), List.of())
                        .stream().anyMatch(s -> !s.isCompleted()))
                .findFirst();

        long missionCompleted = missions.stream()
                .filter(m -> stepsByMission.getOrDefault(m.getId(), List.of())
                        .stream().allMatch(MissionStep::isCompleted))
                .count();

        Integer currentOrder = currentOpt.map(Mission::getOrderNo).orElse(null);
        int remaining = Math.max(0, missionTotal - (int) missionCompleted - (currentOpt.isPresent() ? 1 : 0));

        StageProgress stage = StageProgress.builder()
                .total(missionTotal)
                .currentOrder(currentOrder)
                .remaining(remaining)
                .build();

        // 5. 현재 미션 상세 퍼센트(카드 하단)
        // - curPercent = (현재 미션 완료스텝 / 전체스텝) * 100
        // - 스텝 0개면 상태 COMPLETED면 100, 아니면 0
        CurrentMissionProgress currentMission = currentOpt.map(m -> {
            List<MissionStep> ms = stepsByMission.getOrDefault(m.getId(), List.of());
            int curTotal = ms.size();
            long curDone = ms.stream().filter(MissionStep::isCompleted).count();
            int curPercent;
            if (curTotal == 0) {
                curPercent = (m.getStatus() == MissionStatus.COMPLETED) ? 100 : 0;
            } else {
                curPercent = (int) Math.round(curDone * 100.0 / curTotal);
            }
            return CurrentMissionProgress.builder()
                    .id(m.getId())
                    .title(m.getTitle())
                    .orderNo(m.getOrderNo())
                    .percent(curPercent)
                    .completedSteps((int) curDone)
                    .totalSteps(curTotal)
                    .build();
        }).orElse(null);

        // 6. 응답 조립 (sideJobId는 UserSideJob.id가 아니라 SideJob ID를 내려줌)
        UserSideJobProgressDto progress = UserSideJobProgressDto.builder()
                .percent(overallPercent)
                .stage(stage)
                .currentMission(currentMission)
                .build();

        return UserSideJobProgressResponse.builder()
                .sideJobId(sideJobId)
                .title(title)
                .progress(progress)
                .build();
    }
}
