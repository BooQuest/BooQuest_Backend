package com.booquest.booquest_api.application.service.character;

import com.booquest.booquest_api.application.port.in.character.CreateCharacterUseCase;
import com.booquest.booquest_api.application.port.out.character.CharacterCommandPort;
import com.booquest.booquest_api.domain.character.enums.CharacterType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CharacterService implements CreateCharacterUseCase {

    private final CharacterCommandPort characterCommandPort;

    @Override
    @Transactional
    public void createCharacter(long userId, CharacterType type) {
        UserCharacter character = UserCharacter.builder()
                .userId(userId)
                .name("부냥이")
                .characterType(type)
                .avatarUrl("fake-url")
                .level(1)
                .exp(0)
                .build();

        characterCommandPort.save(character);
    }
}
