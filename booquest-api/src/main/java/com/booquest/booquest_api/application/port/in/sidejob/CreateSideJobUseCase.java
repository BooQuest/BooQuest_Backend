package com.booquest.booquest_api.application.port.in.sidejob;

import com.booquest.booquest_api.domain.sidejob.model.SideJob;

public interface CreateSideJobUseCase {
    SideJob create(String sideJobTitle, Long userId);
}
