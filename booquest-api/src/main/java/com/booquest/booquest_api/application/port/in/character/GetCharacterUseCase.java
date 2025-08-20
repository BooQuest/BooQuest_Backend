package com.booquest.booquest_api.application.port.in.character;

import com.booquest.booquest_api.adapter.in.character.web.dto.CharacterGrowthResponse;
import com.booquest.booquest_api.adapter.in.character.web.dto.UserCharacterResponse;

public interface GetCharacterUseCase {
    UserCharacterResponse getCharacterByUserId(Long userId);
    CharacterGrowthResponse getCharacterGrowthByUserId(Long userId);
}
