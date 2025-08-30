package com.booquest.booquest_api.application.service.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepUpdateStatusResponse;
import com.booquest.booquest_api.application.port.in.missionstep.UpdateMissionStepStatusUseCase;
import com.booquest.booquest_api.application.port.out.bonus.BonusStatusPort;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.application.service.character.CharacterRewardService;
import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import com.booquest.booquest_api.domain.character.policy.CharacterRewardPolicy;
import com.booquest.booquest_api.domain.character.policy.LevelingPolicy;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateMissionStepStatusService implements UpdateMissionStepStatusUseCase {
    private final MissionStepRepositoryPort missionStepRepository;
    private final MissionRepositoryPort missionRepository;
    private final CharacterRewardService characterRewardService;
    private final CharacterRewardPolicy characterRewardPolicy;
    private final LevelingPolicy levelingPolicy;
    private final BonusStatusPort bonusStatusPort;

    @Override
    @Transactional
    public MissionStepUpdateStatusResponse updateStatus(Long stepId, Long userId, StepStatus newStatus) {
        MissionStep step = getMissionStep(stepId);
        Mission mission = getMission(step.getMission().getId());
        checkUserPermission(mission, userId);

        StepStatus oldStatus = step.getStatus();

        if (oldStatus == newStatus) {
            return buildResponse(step, mission, userId, 0, false, 0, 0, 0, false, false);
        }

        // 이전 레벨 저장
        UserCharacter currentCharacter = characterRewardService.getCharacter(userId);
        int previousLevel = currentCharacter.getLevel();

        // 부퀘스트 상태 업데이트
        updateStepStatus(step, newStatus);

        // 경험치 계산 및 적용 (부퀘스트 완료/취소만)
        int totalExpDelta = calculateTotalExpDelta(step, oldStatus, newStatus, userId);

        // 캐릭터에 경험치 적용
        UserCharacter updatedCharacter = characterRewardService.applyExpDelta(userId, totalExpDelta, levelingPolicy);
        
        // 레벨업 정보 계산
        int levelUpCount = updatedCharacter.getLevel() - previousLevel;
        
        // 광고/인증 보너스 상태 확인
        boolean hasAdBonus = bonusStatusPort.hasAdBonus(stepId, userId);
        boolean hasProofBonus = bonusStatusPort.hasProofBonus(stepId, userId);

        return buildResponse(step, mission, userId, totalExpDelta, false, 0, levelUpCount, previousLevel, hasAdBonus, hasProofBonus);
    }

    private MissionStep getMissionStep(Long stepId) {
        return missionStepRepository.findById(stepId)
                .orElseThrow(() -> new EntityNotFoundException("Mission Step not found: " + stepId));
    }

    private Mission getMission(Long missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("Mission not found: " + missionId));
    }

    private void checkUserPermission(Mission mission, Long userId) {
        if (!mission.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You do not have permission to update this step");
        }
    }

    private void updateStepStatus(MissionStep step, StepStatus newStatus) {
        step.updateStatus(newStatus);
        missionStepRepository.save(step);
    }

    private int calculateTotalExpDelta(MissionStep step, StepStatus oldStatus, StepStatus newStatus, Long userId) {
        int baseExpDelta = 0;
        
        // 기본 부퀘스트 완료/취소 경험치
        if (oldStatus != StepStatus.COMPLETED && newStatus == StepStatus.COMPLETED) {
            baseExpDelta = characterRewardPolicy.expDeltaFor(RewardType.STEP_COMPLETED);
        } else if (oldStatus == StepStatus.COMPLETED && newStatus != StepStatus.COMPLETED) {
            baseExpDelta = characterRewardPolicy.expDeltaFor(RewardType.STEP_MARKED_INCOMPLETE);
        }

        // 광고/인증 보너스 경험치 추가
        if (newStatus == StepStatus.COMPLETED) {
            if (bonusStatusPort.hasAdBonus(step.getId(), userId)) {
                baseExpDelta += characterRewardPolicy.expDeltaFor(RewardType.AD_WATCHED);
            }
            if (bonusStatusPort.hasProofBonus(step.getId(), userId)) {
                baseExpDelta += characterRewardPolicy.expDeltaFor(RewardType.PROOF_VERIFIED);
            }
        }

        return baseExpDelta;
    }

    private MissionStepUpdateStatusResponse buildResponse(MissionStep step, Mission mission, Long userId, 
                                                        int expDelta, boolean missionCompleted, int missionExpReward,
                                                        int levelUpCount, int previousLevel, 
                                                        boolean hasAdBonus, boolean hasProofBonus) {
        UserCharacter character = characterRewardService.getCharacter(userId);
        return MissionStepUpdateStatusResponse.toResponse(step, character, expDelta, missionCompleted, 
                                                        missionExpReward, levelUpCount, previousLevel, 
                                                        hasAdBonus, hasProofBonus);
    }
}
