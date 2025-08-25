package com.booquest.booquest_api.domain.character.policy;

import com.booquest.booquest_api.domain.character.enums.RewardType;

public interface CharacterRewardPolicy {
    int expDeltaFor(RewardType type);
}
