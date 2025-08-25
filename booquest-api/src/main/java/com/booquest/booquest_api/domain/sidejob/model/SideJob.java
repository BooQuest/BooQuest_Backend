package com.booquest.booquest_api.domain.sidejob.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.mission.model.Mission;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "sidejob_id")  // Mission 테이블의 FK 컬럼 이름
    @OrderBy("orderNo ASC")
    private List<Mission> missions = new ArrayList<>();
}
