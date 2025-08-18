package com.booquest.booquest_api.application.port.out.sidejob;


import com.booquest.booquest_api.domain.sidejob.model.SideJob;

import java.util.Optional;

public interface SideJobRepositoryPort {
    SideJob save(SideJob sideJob);
    int updateSelectedTrue(Long id);
    Optional<SideJob> findByIdWithMissionsAndSteps(Long id);
}
