package com.booquest.booquest_api.adapter.out.income.persistence;

import com.booquest.booquest_api.domain.income.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    // 사용자 전체 (최신순)
    List<Income> findByUserIdOrderByIncomeDateDesc(Long userId);

    // 특정 부업 (최신순)
    List<Income> findByUserIdAndUserSideJobIdOrderByIncomeDateDesc(Long userId, Long userSideJobId);

    // 특정 기간
    List<Income> findByUserIdAndIncomeDateBetweenOrderByIncomeDateDesc(Long userId, LocalDate startDate, LocalDate endDate);

    // 특정 부업 + 특정 기간
    List<Income> findByUserIdAndUserSideJobIdAndIncomeDateBetweenOrderByIncomeDateDesc(Long userId, Long userSideJobId, LocalDate startDate, LocalDate endDate);

    // 수익 계산
    @Query("select COALESCE(sum(i.amount), 0) from Income i where i.user.id = :userId")
    Long sumAmountByUserId(@Param("userId") Long userId);

    @Query("select COALESCE(sum(i.amount), 0) from Income i where i.user.id = :userId and i.userSideJobId = :userSideJobId")
    Long sumAmountByUserIdAndUserSideJobId(@Param("userId") Long userId, @Param("userSideJobId") Long userSideJobId);

    // 개수
    Long countByUserId(Long userId);

    Long countByUserIdAndUserSideJobId(Long userId, Long userSideJobId);

    // 조회 및 권한 확인
    Optional<Income> findByIdAndUserId(Long id, Long userId);

    @Query("select coalesce(sum(i.amount), 0) from Income i where i.user.id = :userId and i.userSideJobId = :userSideJobId")
    Long sumAmountByUserAndSideJob(@Param("userId") Long userId,
                                   @Param("userSideJobId") Long userSideJobId);

    @Query("select min(i.incomeDate) from Income i where i.user.id = :userId and i.userSideJobId = :userSideJobId")
    LocalDate findFirstIncomeDate(@Param("userId") Long userId,
                                              @Param("userSideJobId") Long userSideJobId);
}
