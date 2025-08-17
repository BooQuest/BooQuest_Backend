package com.booquest.booquest_api.application.port.out.character;

import com.booquest.booquest_api.domain.character.model.UserCharacter;

public interface CharacterCommandPort {
    UserCharacter save(UserCharacter userCharacter);
}
