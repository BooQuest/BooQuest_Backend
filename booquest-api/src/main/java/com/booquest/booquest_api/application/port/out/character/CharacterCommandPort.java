package com.booquest.booquest_api.application.port.out.character;

import com.booquest.booquest_api.domain.character.model.UserCharacter;
import java.util.Optional;

public interface CharacterCommandPort {
    UserCharacter save(UserCharacter userCharacter);

    Optional<UserCharacter> findByUserId(Long userId);

    long deleteByUserId(Long userId);
}
