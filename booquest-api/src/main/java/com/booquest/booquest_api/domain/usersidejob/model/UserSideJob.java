package com.booquest.booquest_api.domain.usersidejob.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.usersidejob.enums.UserSideJobStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_side_jobs")
@Builder
@Getter
public class UserSideJob extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK 제약은 DB에만 있고, 엔티티는 값 매핑만 (히스토리 보존 목적)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "side_job_id", nullable = false)
    private Long sideJobId;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "sidejob_status")
    private UserSideJobStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;
}
