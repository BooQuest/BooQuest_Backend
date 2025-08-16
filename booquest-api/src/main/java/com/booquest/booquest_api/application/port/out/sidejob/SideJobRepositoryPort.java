package com.booquest.booquest_api.application.port.out.sidejob;


import com.booquest.booquest_api.domain.sidejob.model.SideJob;

public interface SideJobRepositoryPort {
    SideJob save(SideJob sideJob);
    int updateSelectedFalse(Long id);
}
