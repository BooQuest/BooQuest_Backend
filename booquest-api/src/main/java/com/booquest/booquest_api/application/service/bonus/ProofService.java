package com.booquest.booquest_api.application.service.bonus;

import com.booquest.booquest_api.adapter.in.bonus.dto.BonusResponse;
import com.booquest.booquest_api.adapter.in.bonus.dto.ProofRequest;
import com.booquest.booquest_api.application.port.in.bonus.ProofUseCase;
import com.booquest.booquest_api.application.port.in.character.UpdateCharacterExpUseCase;
import com.booquest.booquest_api.application.port.out.bonus.AdViewRepositoryPort;
import com.booquest.booquest_api.application.port.out.bonus.ProofRepositoryPort;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.domain.bonus.enums.BonusStatus;
import com.booquest.booquest_api.domain.bonus.model.Proof;
import com.booquest.booquest_api.domain.character.enums.RewardType;
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
public class ProofService implements ProofUseCase {
    private final ProofRepositoryPort proofRepositoryPort;
    private final MissionStepRepositoryPort missionStepRepositoryPort;
    private final MissionRepositoryPort missionRepositoryPort;
    private final AdViewRepositoryPort adViewRepositoryPort;
    private final UpdateCharacterExpUseCase updateCharacterExpUseCase;
    private final CharacterRewardPolicy rewardPolicy;

    @Override
    @Transactional
    public BonusResponse submitProofAndGrantExp(Long userId, Long stepId, ProofRequest request) {
        var check = checkUserPermissionAndStepCompleted(userId, stepId);
        if (!check.completed) {
            return new BonusResponse(BonusStatus.NOT_COMPLETED, 0);
        }

        if (adViewRepositoryPort.existsCompletedByUserIdAndStepId(userId, stepId)) {
            return new BonusResponse(BonusStatus.BLOCKED_BY_AD, 0);
        }

        if (proofRepositoryPort.existsByUserIdAndStepId(userId, stepId)) {
            return new BonusResponse(BonusStatus.ALREADY_VERIFIED, 0);
        }

        Proof proof = Proof.builder()
                .userId(userId)
                .stepId(stepId)
                .proofType(request.getProofType())
                .content(request.getContent())
                .isVerified(true)
                .build();
        proofRepositoryPort.save(proof);

        updateCharacterExpUseCase.applyReward(userId, RewardType.PROOF_VERIFIED);

        int additionalExp = rewardPolicy.expDeltaFor(RewardType.PROOF_VERIFIED);
        return new BonusResponse(BonusStatus.GRANTED, additionalExp);
    }

    private CompletedCheck checkUserPermissionAndStepCompleted(Long userId, Long stepId) {
        MissionStep step = missionStepRepositoryPort.findById(stepId)
                .orElseThrow(() -> new EntityNotFoundException("Mission Step not found: " + stepId));
        Mission mission = missionRepositoryPort.findById(step.getMissionId())
                .orElseThrow(() -> new EntityNotFoundException("Mission not found: " + step.getMissionId()));

        if (!mission.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You do not have permission to access this step");
        }
        boolean completed = (step.getStatus() == StepStatus.COMPLETED);
        return new CompletedCheck(completed);
    }

    private record CompletedCheck(boolean completed) {}
}
