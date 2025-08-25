package com.booquest.booquest_api.adapter.out.character.persistence;

import com.booquest.booquest_api.application.port.in.character.UpdateCharacterExpUseCase;
import com.booquest.booquest_api.application.port.out.character.CharacterRewardPort;
import com.booquest.booquest_api.domain.character.enums.RewardType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CharacterRewardRepositoryAdapter implements CharacterRewardPort {
    private final UpdateCharacterExpUseCase updateCharacterExpUseCase;

    @Override
    public UserCharacter applyReward(Long userId, RewardType type) {
        return updateCharacterExpUseCase.applyReward(userId, type);
    }
}
