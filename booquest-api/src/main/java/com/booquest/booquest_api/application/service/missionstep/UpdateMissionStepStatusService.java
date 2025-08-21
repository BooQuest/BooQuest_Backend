package com.booquest.booquest_api.application.service.missionstep;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepUpdateStatusResponse;
import com.booquest.booquest_api.application.port.in.missionstep.UpdateMissionStepStatusUseCase;
import com.booquest.booquest_api.application.port.out.character.CharacterCommandPort;
import com.booquest.booquest_api.application.port.out.character.CharacterQueryPort;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
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

    private static final int EXP_PER_COMPLETED_MISSION_STEP = 10;

    private final MissionStepRepositoryPort missionStepRepository;
    private final MissionRepositoryPort missionRepository;
    private final CharacterQueryPort characterQueryPort;
    private final CharacterCommandPort characterCommandPort;

    @Override
    @Transactional
    public MissionStepUpdateStatusResponse updateStatus(Long stepId, Long userId, StepStatus newStatus) {
        MissionStep step = getMissionStep(stepId);
        Mission mission = getMission(step.getMissionId());
        checkUserPermission(mission, userId);

        StepStatus oldStatus = step.getStatus();
        if (oldStatus == newStatus) {
            UserCharacter character = getUserCharacter(userId);
            return buildResponse(step, character, 0);
        }

        updateStepStatus(step, newStatus);
        int delta = calculateExpDelta(oldStatus, newStatus);
        UserCharacter character = applyExpDelta(userId, delta);

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

    private UserCharacter getUserCharacter(Long userId) {
        return characterQueryPort.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User character not found: " + userId));
    }

    private void updateStepStatus(MissionStep step, StepStatus newStatus) {
        step.updateStatus(newStatus);
        missionStepRepository.save(step);
    }

    private int calculateExpDelta(StepStatus oldStatus, StepStatus newStatus) {
        if (oldStatus != StepStatus.COMPLETED && newStatus == StepStatus.COMPLETED) return +EXP_PER_COMPLETED_MISSION_STEP;
        if (oldStatus == StepStatus.COMPLETED && newStatus != StepStatus.COMPLETED) return -EXP_PER_COMPLETED_MISSION_STEP;
        return 0;
    }

    private UserCharacter applyExpDelta(Long userId, int delta) {
        UserCharacter character = getUserCharacter(userId);
        if (delta != 0) {
            character.updateExp(character.getExp() + delta);
            characterCommandPort.save(character);
        }
        return character;
    }

    private MissionStepUpdateStatusResponse buildResponse(MissionStep step, UserCharacter character, int delta) {
        return MissionStepUpdateStatusResponse.toResponse(step, character, delta);
    }

}
