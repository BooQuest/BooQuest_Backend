package com.booquest.booquest_api.adapter.out.usersidejob.persistence;

import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSideJobRepository extends JpaRepository<UserSideJob, Long> {

    List<UserSideJob> findAllByUserId(Long userId);

    Optional<UserSideJob> findById(Long userSideJobId);

    boolean existsByUserIdAndSideJobId(Long userId, Long sideJobId);

    Optional<UserSideJob> findByUserIdAndSideJobId(Long userId, Long sideJobId);
}
