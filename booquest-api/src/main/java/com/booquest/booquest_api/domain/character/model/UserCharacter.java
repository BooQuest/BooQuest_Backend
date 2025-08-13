package com.booquest.booquest_api.domain.character.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "step_progress")
@Getter
public class UserCharacter extends AuditableEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String name;

    private int level = 1;

    private int exp = 0;

    @Column(name = "avatar_url")
    private String avatarUrl;

}
