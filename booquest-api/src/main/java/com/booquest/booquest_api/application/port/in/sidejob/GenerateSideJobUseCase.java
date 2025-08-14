package com.booquest.booquest_api.application.port.in.sidejob;

import java.util.List;

public interface GenerateSideJobUseCase {

    long generateSideJob(String userId, String job, List<String> hobbies);
}
