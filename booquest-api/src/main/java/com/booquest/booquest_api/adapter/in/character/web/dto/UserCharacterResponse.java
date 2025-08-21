package com.booquest.booquest_api.adapter.in.character.web.dto;

import com.booquest.booquest_api.domain.character.enums.CharacterType;
import com.booquest.booquest_api.domain.character.model.UserCharacter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCharacterResponse {
    private Long userId;
    private String name;
    private int level;
    private int exp;
    private CharacterType characterType;
    private String avatarUrl;

    public static UserCharacterResponse toResponse(UserCharacter character) {
        return UserCharacterResponse.builder()
                .userId(character.getUserId())
                .name(character.getName())
                .level(character.getLevel())
                .exp(character.getExp())
                .characterType(character.getCharacterType())
                .avatarUrl(character.getAvatarUrl())
                .build();
    }
}
