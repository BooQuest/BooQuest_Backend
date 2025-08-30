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
    private boolean missionCompleted; // 메인퀘스트 완료 여부
    private int missionExpReward; // 메인퀘스트 완료 시 추가 경험치
    private int levelUpCount; // 레벨업 횟수
    private int previousLevel; // 이전 레벨
    private boolean hasAdBonus; // 광고 보너스 적용 여부
    private boolean hasProofBonus; // 인증 보너스 적용 여부

    public static MissionStepUpdateStatusResponse toResponse(MissionStep updatedStep,
                                                             UserCharacter updatedCharacter,
                                                             int expDelta,
                                                             boolean missionCompleted,
                                                             int missionExpReward,
                                                             int levelUpCount,
                                                             int previousLevel,
                                                             boolean hasAdBonus,
                                                             boolean hasProofBonus) {
        return MissionStepUpdateStatusResponse.builder()
                .step(MissionStepResponse.toResponse(updatedStep))
                .character(UserCharacterResponse.toResponse(updatedCharacter))
                .expDelta(expDelta)
                .missionCompleted(missionCompleted)
                .missionExpReward(missionExpReward)
                .levelUpCount(levelUpCount)
                .previousLevel(previousLevel)
                .hasAdBonus(hasAdBonus)
                .hasProofBonus(hasProofBonus)
                .build();
    }
}
