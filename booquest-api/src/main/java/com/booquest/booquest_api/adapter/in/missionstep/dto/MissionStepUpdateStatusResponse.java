package com.booquest.booquest_api.adapter.in.missionstep.dto;

import com.booquest.booquest_api.adapter.in.character.web.dto.UserCharacterResponse;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionStepUpdateStatusResponse {
    private MissionStepResponse step;
    private UserCharacterResponse character;
    private int expDelta; // 경험치: 완료 +10, 완료취소 -10, 그 외 0
    private boolean leveledUp; // 이번 처리에서 레벨업 여부
    private int currentLevel; // 레벨업 후 최종 레벨

    public static MissionStepUpdateStatusResponse toResponse(MissionStep updatedStep,
                                                             UserCharacter updatedCharacter,
                                                             int expDelta,
                                                             int previousLevel) {
        return MissionStepUpdateStatusResponse.builder()
                .step(MissionStepResponse.toResponse(updatedStep))
                .character(UserCharacterResponse.toResponse(updatedCharacter))
                .expDelta(expDelta)
                .leveledUp(updatedCharacter.getLevel() > previousLevel)
                .currentLevel(updatedCharacter.getLevel())
                .build();
    }
}
