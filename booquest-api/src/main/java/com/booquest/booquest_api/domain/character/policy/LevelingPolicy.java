package com.booquest.booquest_api.domain.character.policy;

import com.booquest.booquest_api.domain.character.model.UserCharacter;

public interface LevelingPolicy {
    int expPerLevel(UserCharacter character);
}
