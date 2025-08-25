package com.booquest.booquest_api.domain.character.policy;

import com.booquest.booquest_api.domain.character.enums.RewardType;
import org.springframework.stereotype.Service;

@Service
public class FixedPerCompletionPolicy implements CharacterRewardPolicy {
    private static final int PER_STEP = 10;

    @Override
    public int expDeltaFor(RewardType type) {
        return switch (type) {
            case STEP_COMPLETED         -> +PER_STEP;
            case STEP_MARKED_INCOMPLETE -> -PER_STEP;
            default                     -> 0;
        };
    }
}
