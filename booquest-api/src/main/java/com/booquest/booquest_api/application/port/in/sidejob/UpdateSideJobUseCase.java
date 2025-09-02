package com.booquest.booquest_api.application.port.in.sidejob;

import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;

public interface UpdateSideJobUseCase {
    void clearAllSelected(List<SideJob> sideJobs);

    void markSelected(SideJob sideJob);
}
