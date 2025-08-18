package com.booquest.booquest_api.domain.sidejob.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "side_jobs")
@Builder
@Getter
public class SideJob extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String title;

    private String description;

    @Column(name = "prompt_meta", columnDefinition = "TEXT")
    private String promptMeta;

    @Column(name = "is_selected", nullable = false)
    private boolean isSelected = false;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sidejob_id")  // Mission 테이블의 FK 컬럼 이름
    private Set<Mission> missions;
}
