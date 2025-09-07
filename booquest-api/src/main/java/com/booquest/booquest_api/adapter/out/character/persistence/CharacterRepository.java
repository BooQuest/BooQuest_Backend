package com.booquest.booquest_api.adapter.out.character.persistence;

import com.booquest.booquest_api.domain.character.model.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<UserCharacter, Long> {
    Optional<UserCharacter> findByUserId(Long userId);
    long deleteByUserId(Long userId);
}
