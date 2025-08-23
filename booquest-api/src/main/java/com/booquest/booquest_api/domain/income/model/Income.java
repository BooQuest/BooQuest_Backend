package com.booquest.booquest_api.domain.income.model;

import com.booquest.booquest_api.common.entity.AuditableEntity;
import com.booquest.booquest_api.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "income")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Income extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_side_job_id", nullable = false)
    private Long userSideJobId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "income_date", nullable = false)
    private LocalDate incomeDate;

    @Column(name = "memo")
    private String memo;

    @Builder
    public Income(User user, Long userSideJobId, String title, Integer amount, LocalDate incomeDate, String memo) {
        this.user = user;
        this.userSideJobId = userSideJobId;
        this.title = title;
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.memo = memo;
    }

    public void update(String title, Integer amount, LocalDate incomeDate, String memo) {
        this.title = title;
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.memo = memo;
    }
}
