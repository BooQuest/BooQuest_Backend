package com.booquest.booquest_api.domain.missionstep.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.missionstep.enums.StepStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "mission_id", insertable = false, updatable = false)
    private Long missionId; // read-only

    @Column(nullable = false)
    private int seq;

    private String title;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "status")
    @Builder.Default
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
}
