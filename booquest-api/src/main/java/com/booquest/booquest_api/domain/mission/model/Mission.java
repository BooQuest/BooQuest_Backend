package com.booquest.booquest_api.domain.mission.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.sidejob.enums.MissionStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "missions")
@Builder
@Getter
public class Mission extends AuditableEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sidejob_id", nullable = false)
    private Long sidejobId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String title;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "mission_status")
    private MissionStatus status = MissionStatus.PLANNED;

    @Column(name = "order_no", nullable = false)
    private int orderNo;

    @Column(name = "design_notes", columnDefinition = "TEXT")
    private String designNotes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mission_id")
    private Set<MissionStep> steps;
}
