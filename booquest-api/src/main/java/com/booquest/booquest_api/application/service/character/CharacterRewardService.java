package com.booquest.booquest_api.application.service.character;

import com.booquest.booquest_api.application.port.in.character.UpdateCharacterExpUseCase;
import com.booquest.booquest_api.application.port.out.character.CharacterCommandPort;
import com.booquest.booquest_api.application.port.out.character.CharacterQueryPort;
import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import com.booquest.booquest_api.domain.character.policy.CharacterRewardPolicy;
import com.booquest.booquest_api.domain.character.policy.FixedLevelingPolicy;
import com.booquest.booquest_api.domain.character.policy.FixedPerCompletionPolicy;
import com.booquest.booquest_api.domain.character.policy.LevelingPolicy;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CharacterRewardService implements UpdateCharacterExpUseCase {
    private final CharacterQueryPort characterQueryPort;
    private final CharacterCommandPort characterCommandPort;

    private final CharacterRewardPolicy policy = new FixedPerCompletionPolicy();
    private final LevelingPolicy levelingPolicy = new FixedLevelingPolicy();

    @Override
    @Transactional
    public UserCharacter applyReward(Long userId, RewardType type) {
        UserCharacter userCharacter = characterQueryPort.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User character not found: " + userId));
        if (type == RewardType.NONE) {
            return userCharacter;
        }

        int delta = policy.expDeltaFor(type);
        if (delta != 0) {
            userCharacter.applyExpDelta(delta, levelingPolicy);
            characterCommandPort.save(userCharacter);
        }
        return userCharacter;
    }

    @Transactional(readOnly = true)
    public UserCharacter getCharacter(Long userId) {
        return characterQueryPort.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User character not found: " + userId));
    }

    @Transactional
    public UserCharacter applyExpDelta(Long userId, int expDelta, LevelingPolicy levelingPolicy) {
        UserCharacter userCharacter = characterQueryPort.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User character not found: " + userId));
        
        if (expDelta != 0) {
            userCharacter.applyExpDelta(expDelta, levelingPolicy);
            characterCommandPort.save(userCharacter);
        }
        
        return userCharacter;
    }
}
