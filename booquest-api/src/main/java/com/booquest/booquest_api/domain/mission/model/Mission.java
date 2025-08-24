package com.booquest.booquest_api.domain.mission.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sidejob_id", nullable = false)
    private SideJob sideJob;

    @Column(name = "sidejob_id", insertable = false, updatable = false)
    private Long sideJobId; // read-only

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String title;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "mission_status")
    @Builder.Default
    private MissionStatus status = MissionStatus.PLANNED;

    @Column(name = "order_no", nullable = false)
    private int orderNo;

    @Column(name = "design_notes", columnDefinition = "TEXT")
    private String designNotes;

    @OneToMany(
            mappedBy = "mission",   // 자식(MissionStep) 쪽 필드명
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<MissionStep> steps = new java.util.HashSet<>();
//    @OrderBy("seq ASC")
//    private List<MissionStep> steps = new ArrayList<>();

    public void updateTitleAndNotes(String title, String notes) {
        this.title = title;
        this.designNotes = notes;
    }

    public void startWithSteps() {
        if (this.status != MissionStatus.PLANNED) {
            throw new IllegalStateException("미션은 PLANNED 상태에서만 시작할 수 있습니다.");
        }

        if (steps.size() != 5) {
            throw new IllegalStateException("미션을 시작하려면 5개의 미션 스텝이 필요합니다.");
        }

        // 첫 번째 step을 찾아서 시작
        MissionStep firstStep = steps.stream()
                .filter(step -> step.getSeq() == 1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("첫 번째 미션 스텝(seq=1)이 존재하지 않습니다."));

        this.status = MissionStatus.IN_PROGRESS;
        firstStep.start();
    }
}
