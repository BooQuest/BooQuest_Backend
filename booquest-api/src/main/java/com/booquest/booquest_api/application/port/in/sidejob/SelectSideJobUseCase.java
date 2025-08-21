package com.booquest.booquest_api.application.port.in.sidejob;


import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;

public interface SelectSideJobUseCase {
    SideJob selectSideJob(Long sideJobId);

    List<SideJob> selectSideJobsByUserId(Long userId);
}
