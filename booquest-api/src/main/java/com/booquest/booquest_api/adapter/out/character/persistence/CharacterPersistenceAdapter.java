package com.booquest.booquest_api.adapter.out.character.persistence;

import com.booquest.booquest_api.application.port.out.character.CharacterCommandPort;
import com.booquest.booquest_api.application.port.out.character.CharacterQueryPort;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CharacterPersistenceAdapter implements CharacterCommandPort, CharacterQueryPort {

    private final CharacterRepository characterRepository;
    @Override
    public UserCharacter save(UserCharacter userCharacter) {
        return characterRepository.save(userCharacter);
    }

    @Override
    public Optional<UserCharacter> findByUserId(Long userId) {
        return characterRepository.findById(userId);
    }

    @Override
    public long deleteByUserId(Long userId) {
        return characterRepository.deleteByUserId(userId);
    }
}
