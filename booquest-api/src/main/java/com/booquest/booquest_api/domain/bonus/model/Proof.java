package com.booquest.booquest_api.domain.bonus.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.bonus.enums.ProofType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "proofs")
@Getter
@Builder
public class Proof extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "step_id", nullable = false)
    private Long stepId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "proof_type", nullable = false)
    private ProofType proofType;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content; // link, text, imageUrl

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    public void verify() {
        this.isVerified = true;
    }
}