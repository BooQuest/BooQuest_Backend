package com.booquest.booquest_api.adapter.in.character.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterGrowthResponse {
    private String name;
    private String type;
    private int level;
    private int remainingExpToLevelUp;
    private int currentExp;
    private int requiredExpForNextLevel;
}
