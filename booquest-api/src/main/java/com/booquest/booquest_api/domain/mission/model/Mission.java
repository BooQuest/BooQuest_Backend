package com.booquest.booquest_api.domain.mission.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.sidejob.enums.MissionStatus;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "missions")
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
    private MissionStatus status = MissionStatus.PLANNED;

    @Column(name = "order_no", nullable = false)
    private int orderNo;

    @Type(JsonType.class)
    @Column(name = "design_notes", columnDefinition = "jsonb")
    private String designNotes;
}
