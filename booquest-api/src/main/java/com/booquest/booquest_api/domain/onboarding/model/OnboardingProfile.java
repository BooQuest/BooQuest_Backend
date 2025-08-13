package com.booquest.booquest_api.domain.onboarding.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "onboarding_profiles")
@Getter
public class OnboardingProfile extends AuditableEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private String metadata;
}
