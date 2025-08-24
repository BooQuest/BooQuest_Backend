package com.booquest.booquest_api.domain.sidejob.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.*;
import org.hibernate.annotations.Type;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "side_jobs")
@Builder
@Getter
@Setter
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

    @OneToMany(
            mappedBy = "sideJob",   // 자식(Mission) 쪽 필드명
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<Mission> missions = new java.util.HashSet<>();
}
