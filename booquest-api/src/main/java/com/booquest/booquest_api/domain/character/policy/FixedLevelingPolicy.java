package com.booquest.booquest_api.domain.character.policy;

import com.booquest.booquest_api.domain.character.model.UserCharacter;
import org.springframework.stereotype.Component;

@Component
public class FixedLevelingPolicy implements LevelingPolicy {
    private static final int EXP_PER_LEVEL = 150;

    @Override
    public int expPerLevel(UserCharacter character) {
        return EXP_PER_LEVEL;   // 나중에 캐릭터 타입/레벨별로 다르게 하고 싶으면 여기서 조절
    }
}
