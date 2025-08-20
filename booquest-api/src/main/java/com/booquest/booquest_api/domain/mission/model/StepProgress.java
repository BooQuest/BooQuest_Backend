package com.booquest.booquest_api.domain.mission.model;

import com.booquest.booquest_api.common.entity.CreatedOnlyEntity;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "step_progress")
@Getter
public class StepProgress extends CreatedOnlyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_id", nullable = false)
    private Long stepId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private StepStatus status = StepStatus.PLANNED;

    @Column(name = "difficulty_rate")
    private Integer difficultyRate;

    @Column(name = "prefer_similar")
    private Boolean preferSimilar;

    @Column(name = "feedback_text")
    private String feedbackText;

    @Column(columnDefinition = "TEXT")
    private String extra;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
