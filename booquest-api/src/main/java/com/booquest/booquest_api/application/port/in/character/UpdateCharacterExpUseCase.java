package com.booquest.booquest_api.application.port.in.character;

import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;

public interface UpdateCharacterExpUseCase {
    UserCharacter applyReward(Long userId, RewardType type);
}
