package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.application.dto.SideJobGenerationResult;
import com.booquest.booquest_api.application.dto.SideJobItem;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
import com.booquest.booquest_api.application.port.out.ai.AiClientSideJobPort;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepository;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.ArrayList;
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
    public List<SideJob> generateSideJob(long userId, String job, List<String> hobbies, String desiredSideJob) {
        SideJobGenerationResult result = aiClientPort.generateSideJob(job, hobbies, desiredSideJob);

        List<SideJob> savedJobs = new ArrayList<>();
        List<SideJobItem> tasks = result.tasks();
        for (SideJobItem task : tasks) {
            SideJob sideJob = SideJob.builder()
                    .userId(userId)
                    .title(task.title())
                    .description(task.description())
                    .promptMeta(result.prompt())
                    .isSelected(false)
                    .build();

            savedJobs.add(sideJobRepository.save(sideJob));
        }

        return savedJobs;
    }
}
