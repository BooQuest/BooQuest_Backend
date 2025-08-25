package com.booquest.booquest_api.application.port.out.character;

import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;

public interface CharacterRewardPort {
    UserCharacter applyReward(Long userId, RewardType type);
}
