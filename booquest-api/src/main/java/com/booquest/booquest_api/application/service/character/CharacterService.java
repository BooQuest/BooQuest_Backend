package com.booquest.booquest_api.application.service.character;

import com.booquest.booquest_api.adapter.in.character.web.dto.CharacterGrowthResponse;
import com.booquest.booquest_api.adapter.in.character.web.dto.UserCharacterResponse;
import com.booquest.booquest_api.application.port.in.character.CreateCharacterUseCase;
import com.booquest.booquest_api.application.port.in.character.GetCharacterUseCase;
import com.booquest.booquest_api.application.port.out.character.CharacterCommandPort;
import com.booquest.booquest_api.application.port.out.character.CharacterQueryPort;
import com.booquest.booquest_api.domain.character.enums.CharacterType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CharacterService implements CreateCharacterUseCase, GetCharacterUseCase {

    private final CharacterCommandPort characterCommandPort;
    private final CharacterQueryPort characterQueryPort;

//    @Value("${booquest.assets.avatar-base-url:/assets/avatars}")
//    private String avatarBaseUrl;

    @Override
    @Transactional
    public void createCharacter(long userId, CharacterType type) {
        UserCharacter character = characterCommandPort.findByUserId(userId)
                .map(existing -> existing.withCharacterType(type))
                .orElseGet(() -> UserCharacter.builder()
                        .userId(userId)
                        .name(defaultName())
                        .characterType(type)
                        .avatarUrl(null)
//                        .avatarUrl(defaultAvatarUrl(type))
                        .level(1)
                        .exp(0)
                        .build()
                );

        characterCommandPort.save(character);
    }

    private String defaultName() {
        return "부냥이";
    }

//    private String defaultAvatarUrl(CharacterType type) {
//        return String.format("", avatarBaseUrl, type.name().toLowerCase());
//    }

    @Override
    @Transactional(readOnly = true)
    public UserCharacterResponse getCharacterByUserId(Long userId) {
        UserCharacter userCharacter = characterQueryPort.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User character not found: " + userId));
        return UserCharacterResponse.toResponse(userCharacter);
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterGrowthResponse getCharacterGrowthByUserId(Long userId) {
        UserCharacter userCharacter = characterQueryPort.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("User character not found: " + userId));

        String name = userCharacter.getName();
        String type = userCharacter.getCharacterType().name();
        int currentLevel = userCharacter.getLevel();
        int currentExp = userCharacter.getExp();
        int requiredForNext = 150;
        int remainingToLevelUp = Math.max(0, requiredForNext - currentExp);

        return CharacterGrowthResponse.builder()
                .name(name)
                .type(type)
                .level(currentLevel)
                .remainingExpToLevelUp(remainingToLevelUp)
                .currentExp(currentExp)
                .requiredExpForNextLevel(requiredForNext)
                .build();
    }
}
