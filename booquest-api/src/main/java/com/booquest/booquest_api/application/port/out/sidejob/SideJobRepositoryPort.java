package com.booquest.booquest_api.application.port.out.sidejob;


import com.booquest.booquest_api.domain.sidejob.model.SideJob;

import java.util.List;
import java.util.Optional;

public interface SideJobRepositoryPort {
    SideJob save(SideJob sideJob);
    int updateSelectedTrue(Long id);
    Optional<SideJob> findByIdWithMissionsAndSteps(Long id);

    boolean isExistByUserId(Long userId);

    List<SideJob> findAllByIds(List<Long> sideJobIds);

    List<SideJob> findAllByUserId(Long userId);

    List<SideJob> findTop3ByUserIdOrderByCreatedAtDesc(Long userId);

    Long findSelectedSideJobIdByUserId(Long userId);

    void deleteAll(List<SideJob> sideJobs);

    Optional<SideJob> findByIdAndUserId(Long sideJobId, Long userId);

    void delete(SideJob sideJob);

    Optional<SideJob> findById(Long sideJobSelectedId);

    long deleteByUserId(Long userId);

    Optional<SideJob> findSelectedSideJobByUserId(Long userId);
}
