package com.booquest.booquest_api.application.service.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepUpdateStatusResponse;
import com.booquest.booquest_api.application.port.in.missionstep.UpdateMissionStepStatusUseCase;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.application.service.character.CharacterRewardService;
import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import com.booquest.booquest_api.domain.character.policy.CharacterRewardPolicy;
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

    @Override
    @Transactional
    public MissionStepUpdateStatusResponse updateStatus(Long stepId, Long userId, StepStatus newStatus) {
        MissionStep step = getMissionStep(stepId);
        Mission mission = getMission(step.getMission().getId());
        checkUserPermission(mission, userId);

        StepStatus oldStatus = step.getStatus();

        if (oldStatus == newStatus) {
            RewardType rewardType = RewardType.NONE;
            UserCharacter character = characterRewardService.applyReward(userId, rewardType);
            int delta = characterRewardPolicy.expDeltaFor(rewardType); // 0
            return buildResponse(step, character, delta);
        }

        updateStepStatus(step, newStatus);

        RewardType rewardType = mapRewardType(oldStatus, newStatus);
        UserCharacter character = characterRewardService.applyReward(userId, rewardType);
        int delta = characterRewardPolicy.expDeltaFor(rewardType);
        return buildResponse(step, character, delta);
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

    private RewardType mapRewardType(StepStatus oldStatus, StepStatus newStatus) {
        if (oldStatus != StepStatus.COMPLETED && newStatus == StepStatus.COMPLETED) return RewardType.STEP_COMPLETED;
        if (oldStatus == StepStatus.COMPLETED && newStatus != StepStatus.COMPLETED) return RewardType.STEP_MARKED_INCOMPLETE;
        return RewardType.NONE;
    }

    private MissionStepUpdateStatusResponse buildResponse(MissionStep step, UserCharacter character, int delta) {
        return MissionStepUpdateStatusResponse.toResponse(step, character, delta);
    }

}
