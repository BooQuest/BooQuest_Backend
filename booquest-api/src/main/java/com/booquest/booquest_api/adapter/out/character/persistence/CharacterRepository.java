package com.booquest.booquest_api.adapter.out.character.persistence;

import com.booquest.booquest_api.domain.character.model.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<UserCharacter, Long> {
    long deleteByUserId(Long userId);
}
