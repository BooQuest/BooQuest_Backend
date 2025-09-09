package com.booquest.booquest_api.adapter.in.mission.dto;

import com.booquest.booquest_api.adapter.in.character.web.dto.UserCharacterResponse;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import com.booquest.booquest_api.domain.mission.enums.MissionCompleteStatus;
import com.booquest.booquest_api.domain.mission.model.Mission;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MissionCompleteResponse {
    private MissionCompleteStatus status;
    private MissionResponse mission;
    private UserCharacterResponse character;
    private int totalExpReward; // 총 추가된 경험치 (부퀘스트 + 광고/인증 + 메인퀘스트 완료)
    private int stepExpReward; // 부퀘스트 완료 경험치
    private int bonusExpReward; // 광고/인증 추가 경험치
    private int missionCompletionExpReward; // 메인퀘스트 완료 경험치
    private int levelUpCount; // 레벨업 횟수
    private int previousLevel; // 이전 레벨
    private boolean missionCompleted; // 메인퀘스트 완료 여부
    private boolean leveledUp; // 이번 처리에서 레벨업 여부
    private int currentLevel; // 레벨업 후 최종 레벨

    public static MissionCompleteResponse toResponse(Mission mission, UserCharacter character,
                                                   int totalExpReward, int stepExpReward, 
                                                   int bonusExpReward, int missionCompletionExpReward,
                                                   int levelUpCount, int previousLevel, 
                                                   boolean missionCompleted) {
        return toResponse(MissionCompleteStatus.COMPLETED, mission, character, totalExpReward, 
                         stepExpReward, bonusExpReward, missionCompletionExpReward, 
                         levelUpCount, previousLevel, missionCompleted);
    }

    public static MissionCompleteResponse toResponse(MissionCompleteStatus status, Mission mission, UserCharacter character,
                                                   int totalExpReward, int stepExpReward, 
                                                   int bonusExpReward, int missionCompletionExpReward,
                                                   int levelUpCount, int previousLevel, 
                                                   boolean missionCompleted) {
        return MissionCompleteResponse.builder()
                .status(status)
                .mission(mission != null ? MissionResponse.toResponse(mission) : null)
                .character(character != null ? UserCharacterResponse.toResponse(character) : null)
                .totalExpReward(totalExpReward)
                .stepExpReward(stepExpReward)
                .bonusExpReward(bonusExpReward)
                .missionCompletionExpReward(missionCompletionExpReward)
                .levelUpCount(levelUpCount)
                .previousLevel(previousLevel)
                .missionCompleted(missionCompleted)
                .leveledUp(levelUpCount > 0)
                .currentLevel(character != null ? character.getLevel() : 0)
                .build();
    }
}