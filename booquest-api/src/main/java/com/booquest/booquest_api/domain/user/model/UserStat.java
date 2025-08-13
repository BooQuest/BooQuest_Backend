package com.booquest.booquest_api.domain.user.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_stats")
@Getter
public class UserStat extends AuditableEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "total_missions", nullable = false)
    private int totalMissions = 0;

    @Column(name = "completed_missions", nullable = false)
    private int completedMissions = 0;

    @Column(name = "completion_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal completionRate = BigDecimal.ZERO;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;
}
