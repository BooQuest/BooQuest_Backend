package com.booquest.booquest_api.application.port.in.character;

import com.booquest.booquest_api.domain.character.enums.CharacterType;

public interface CreateCharacterUseCase {
    void createCharacter(long userId, CharacterType type, String nickname);
}
