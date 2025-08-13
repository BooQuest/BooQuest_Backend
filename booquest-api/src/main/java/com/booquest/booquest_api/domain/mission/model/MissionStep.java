package com.booquest.booquest_api.domain.mission.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.sidejob.enums.StepStatus;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "mission_steps")
@Getter
public class MissionStep extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mission_id", nullable = false)
    private Long missionId;

    @Column(nullable = false)
    private int seq;

    private String title;

    @Enumerated(EnumType.STRING)
    private StepStatus status = StepStatus.PLANNED;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private String detail;
}
