package com.booquest.booquest_api.domain.character.policy;

import com.booquest.booquest_api.domain.character.enums.RewardType;
import org.springframework.stereotype.Service;

@Service
public class FixedPerCompletionPolicy implements CharacterRewardPolicy {
    private static final int PER_STEP = 10;
    private static final int PROOF_REWARD = 10; // 인증 보상
    private static final int AD_REWARD = 10;    // 광고 보상
    private static final int MISSION_COMPLETION_REWARD = 50; // 메인퀘스트 완료 보상

    @Override
    public int expDeltaFor(RewardType type) {
        return switch (type) {
            case STEP_COMPLETED         -> +PER_STEP;
            case STEP_MARKED_INCOMPLETE -> -PER_STEP;
            case PROOF_VERIFIED         -> +PROOF_REWARD;
            case AD_WATCHED             -> +AD_REWARD;
            case MISSION_COMPLETED      -> +MISSION_COMPLETION_REWARD;
            default                     -> 0;
        };
    }
}
