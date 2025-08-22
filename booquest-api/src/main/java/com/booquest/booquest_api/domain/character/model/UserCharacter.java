package com.booquest.booquest_api.domain.character.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.character.enums.CharacterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_characters")
@Getter
@Builder
public class UserCharacter extends AuditableEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String name;

    private int level = 1;

    private int exp = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "character_type")
    private CharacterType characterType;

    @Column(name = "avatar_url")
    private String avatarUrl;

    public void updateExp(int newExp) {
        this.exp = Math.max(0, newExp);
    }

    public UserCharacter withCharacterType(CharacterType type) {
        this.characterType = type;
        return this;
    }
}
