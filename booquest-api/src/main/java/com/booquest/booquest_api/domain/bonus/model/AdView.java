package com.booquest.booquest_api.domain.bonus.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ad_views")
@Getter
@Builder
public class AdView extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "step_id")
    private Long stepId; // null일 수 있음 (step과 무관한 광고 시청) -> ?

    @Column(name = "ad_session_id")
    private String adSessionId;

    @Column(name = "receipt")
    private String receipt;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted = false;

    public void complete() {
        this.isCompleted = true;
    }
}