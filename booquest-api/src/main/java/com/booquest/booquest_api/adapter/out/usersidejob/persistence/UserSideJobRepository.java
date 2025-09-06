package com.booquest.booquest_api.adapter.out.usersidejob.persistence;

import com.booquest.booquest_api.domain.usersidejob.enums.UserSideJobStatus;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSideJobRepository extends JpaRepository<UserSideJob, Long> {

    List<UserSideJob> findAllByUserId(Long userId);

    Optional<UserSideJob> findById(Long userSideJobId);

    boolean existsByUserIdAndSideJobId(Long userId, Long sideJobId);

    Optional<UserSideJob> findByUserIdAndSideJobId(Long userId, Long sideJobId);

    boolean existsByIdAndUserId(Long id, Long userId);

    @Query("select count(usj) from UserSideJob usj " +
            "where usj.userId = :userId and usj.status = :status")
    long countCompletedByUserId(@Param("userId") Long userId,
                                @Param("status") UserSideJobStatus status);

    @Query("""
      select usj
      from UserSideJob usj
      where usj.userId = :uid
        and usj.status = :status
        and coalesce(usj.updatedAt, usj.createdAt) = (
          select max(coalesce(u2.updatedAt, u2.createdAt))
          from UserSideJob u2
          where u2.userId = :uid
            and u2.status  = :status
        )
        and usj.id = (
          select max(u3.id)
          from UserSideJob u3
          where u3.userId = :uid
            and u3.status  = :status
            and coalesce(u3.updatedAt, u3.createdAt) = (
              select max(coalesce(u4.updatedAt, u4.createdAt))
              from UserSideJob u4
              where u4.userId = :uid
                and u4.status  = :status
            )
        )
    """)
    Optional<UserSideJob> findLatestSideJobForStatus(@Param("uid") Long userId,
                                                @Param("status") UserSideJobStatus status);

    long deleteByUserId(Long userId);

    Optional<UserSideJob> findBySideJobId(Long sideJobId);
}
