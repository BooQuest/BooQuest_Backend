package com.booquest.booquest_api.domain.missionstep.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission_steps")
@Builder
@Getter
public class MissionStep extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Column(nullable = false)
    private int seq;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "status")
    private StepStatus status = StepStatus.PLANNED;

    @Column(columnDefinition = "TEXT")
    private String detail;

    public void updateStatus(StepStatus newStatus) {
        this.status = newStatus;
    }

    public void updateTitleAndDetail(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

    public void start() {
        if (this.status != StepStatus.PLANNED) {
            throw new IllegalStateException("스텝은 PLANNED 상태에서만 시작할 수 있습니다.");
        }
        this.status = StepStatus.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return status == StepStatus.COMPLETED;
    }
}
