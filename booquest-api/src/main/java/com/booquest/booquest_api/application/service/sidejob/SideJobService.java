package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.application.dto.SideJobGenerationResult;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
import com.booquest.booquest_api.application.port.out.ai.AiClientSideJobPort;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SideJobService implements GenerateSideJobUseCase {

    private final SideJobRepository sideJobRepository;
    private final AiClientSideJobPort aiClientPort;

    @Transactional
    @Override
    public long generateSideJob(String userId, String job, List<String> hobbies) {
        SideJobGenerationResult result = aiClientPort.generateSideJob(job, hobbies);

        return 0;
    }
}
