package com.booquest.booquest_api.adapter.in.mission.dto;

import com.booquest.booquest_api.adapter.in.character.web.dto.UserCharacterResponse;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import com.booquest.booquest_api.domain.mission.model.Mission;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionCompleteResponse {
    private MissionResponse mission;
    private UserCharacterResponse character;
    private int totalExpReward; // 총 추가된 경험치 (부퀘스트 + 광고/인증 + 메인퀘스트 완료)
    private int stepExpReward; // 부퀘스트 완료 경험치
    private int bonusExpReward; // 광고/인증 추가 경험치
    private int missionCompletionExpReward; // 메인퀘스트 완료 경험치
    private int levelUpCount; // 레벨업 횟수
    private int previousLevel; // 이전 레벨
    private boolean missionCompleted; // 메인퀘스트 완료 여부

    public static MissionCompleteResponse toResponse(Mission mission, UserCharacter character,
                                                   int totalExpReward, int stepExpReward, 
                                                   int bonusExpReward, int missionCompletionExpReward,
                                                   int levelUpCount, int previousLevel, 
                                                   boolean missionCompleted) {
        return MissionCompleteResponse.builder()
                .mission(MissionResponse.toResponse(mission))
                .character(UserCharacterResponse.toResponse(character))
                .totalExpReward(totalExpReward)
                .stepExpReward(stepExpReward)
                .bonusExpReward(bonusExpReward)
                .missionCompletionExpReward(missionCompletionExpReward)
                .levelUpCount(levelUpCount)
                .previousLevel(previousLevel)
                .missionCompleted(missionCompleted)
                .build();
    }
} 