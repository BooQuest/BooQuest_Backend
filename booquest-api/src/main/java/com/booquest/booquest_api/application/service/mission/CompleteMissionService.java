package com.booquest.booquest_api.application.service.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionCompleteResponse;
import com.booquest.booquest_api.application.port.in.mission.CompleteMissionUseCase;
import com.booquest.booquest_api.application.port.out.bonus.BonusStatusPort;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.usersidejob.UserSideJobRepositoryPort;
import com.booquest.booquest_api.application.service.character.CharacterRewardService;
import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import com.booquest.booquest_api.domain.character.policy.CharacterRewardPolicy;
import com.booquest.booquest_api.domain.character.policy.LevelingPolicy;
import com.booquest.booquest_api.domain.mission.enums.MissionCompleteStatus;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteMissionService implements CompleteMissionUseCase {
    private final MissionRepositoryPort missionRepository;
    private final CharacterRewardService characterRewardService;
    private final CharacterRewardPolicy characterRewardPolicy;
    private final LevelingPolicy levelingPolicy;
    private final BonusStatusPort bonusStatusPort;
    private final UserSideJobRepositoryPort userSideJobRepositoryPort;

    @Override
    @Transactional
    public MissionCompleteResponse completeMission(Long missionId, Long userId) {
        try {
            Mission mission = getMission(missionId);
            checkUserPermission(mission, userId);
            checkMissionCanBeCompleted(mission);

            // 이전 레벨 저장
            UserCharacter currentCharacter = characterRewardService.getCharacter(userId);
            int previousLevel = currentCharacter.getLevel();

            // 경험치 계산
            int stepExpReward = calculateStepExpReward(mission);
            int bonusExpReward = calculateBonusExpReward(mission, userId);
            int missionCompletionExpReward = characterRewardPolicy.expDeltaFor(RewardType.MISSION_COMPLETED);
            int totalExpReward = stepExpReward + bonusExpReward + missionCompletionExpReward;

            // 메인퀘스트 완료 처리
            mission.complete();
            missionRepository.save(mission);

            // 캐릭터에 경험치 적용
            UserCharacter updatedCharacter = characterRewardService.applyExpDelta(userId, missionCompletionExpReward, levelingPolicy);

            // 레벨업 정보 계산
            int levelUpCount = updatedCharacter.getLevel() - previousLevel;

            //5단계 메인퀘스트를 클리어하면 부업과 유저 부업을 완료 처리
            if (mission.getOrderNo() == 5){
                clearSideJob(mission);
            }

            return MissionCompleteResponse.toResponse(
                    MissionCompleteStatus.COMPLETED, mission, updatedCharacter, totalExpReward, stepExpReward,
                    bonusExpReward, missionCompletionExpReward, levelUpCount, previousLevel, true
            );
        } catch (EntityNotFoundException e) {
            return MissionCompleteResponse.toResponse(
                    MissionCompleteStatus.MISSION_NOT_FOUND, null, null, 0, 0, 0, 0, 0, 0, false
            );
        } catch (IllegalStateException e) {
            MissionCompleteStatus status;
            if (e.getMessage().contains("already completed")) {
                status = MissionCompleteStatus.ALREADY_COMPLETED;
            } else if (e.getMessage().contains("must be in progress")) {
                status = MissionCompleteStatus.NOT_IN_PROGRESS;
            } else {
                status = MissionCompleteStatus.NOT_ALL_STEPS_COMPLETED;
            }
            return MissionCompleteResponse.toResponse(
                    status, null, null, 0, 0, 0, 0, 0, 0, false
            );
        }
    }

    private void clearSideJob(Mission mission) {
        SideJob sideJob = mission.getSideJob();
        sideJob.complete();
        UserSideJob userSideJob = userSideJobRepositoryPort.findBySideJobId(sideJob.getId())
                .orElseThrow(() -> new IllegalArgumentException("No UserSideJob By SideJob Id"));

        userSideJob.complete();
    }

    private Mission getMission(Long missionId) {
        return missionRepository.findByIdWithSteps(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found: " + missionId));
    }

    private void checkUserPermission(Mission mission, Long userId) {
        if (!mission.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You do not have permission to complete this mission");
        }
    }

    private void checkMissionCanBeCompleted(Mission mission) {
        MissionStatus status = mission.getStatus();
        if (status == MissionStatus.COMPLETED) {
            throw new IllegalStateException("Mission is already completed");
        }
        if (status != MissionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Mission must be in progress to be completed");
        }
        if (!mission.isAllStepsCompleted()) {
            throw new IllegalStateException("All mission steps must be completed before completing the mission");
        }
    }

    private int calculateStepExpReward(Mission mission) {
        Set<MissionStep> steps = mission.getSteps();
        return steps.stream()
                .filter(MissionStep::isCompleted)
                .mapToInt(step -> characterRewardPolicy.expDeltaFor(RewardType.STEP_COMPLETED))
                .sum();
    }

    private int calculateBonusExpReward(Mission mission, Long userId) {
        Set<MissionStep> steps = mission.getSteps();
        int totalBonusExp = 0;

        for (MissionStep step : steps) {
            if (step.isCompleted()) {
                if (bonusStatusPort.hasAdBonus(step.getId(), userId)) {
                    totalBonusExp += characterRewardPolicy.expDeltaFor(RewardType.AD_WATCHED);
                }
                if (bonusStatusPort.hasProofBonus(step.getId(), userId)) {
                    totalBonusExp += characterRewardPolicy.expDeltaFor(RewardType.PROOF_VERIFIED);
                }
            }
        }

        return totalBonusExp;
    }
} 
