package com.booquest.booquest_api.user.domain.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditableEntity {

    @Id @GeneratedValue
    private Long id;

    private String provider;

    private String providerUserId;

    private String email;

    private String nickname;
}
