package com.booquest.booquest_api.application.port.in.sidejob;

public interface DeleteSideJobUseCase {
    void deleteAllSideJob(Long userId);

    void deleteSideJob(Long userId, Long sideJobId);
}
