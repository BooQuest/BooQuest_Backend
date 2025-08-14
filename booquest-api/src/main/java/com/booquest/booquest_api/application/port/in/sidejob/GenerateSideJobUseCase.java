package com.booquest.booquest_api.application.port.in.sidejob;

import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.List;

public interface GenerateSideJobUseCase {

    List<SideJob> generateSideJob(long userId, String job, List<String> hobbies);
}
