package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.SideJobGenerationResult;
import com.booquest.booquest_api.application.port.in.sidejob.SideJobItem;
import com.booquest.booquest_api.application.port.out.sidejob.GenerateSideJobPort;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenerateSideJobService implements GenerateSideJobUseCase {

    @Autowired
    private final SideJobRepositoryPort sideJobRepository;

    private final GenerateSideJobPort sideJobGenerator;

    @Transactional
    @Override
    public List<SideJob> generateSideJob(GenerateSideJobRequest request) {
        SideJobGenerationResult result = sideJobGenerator.generateSideJob(request);

        List<SideJob> savedJobs = new ArrayList<>();
        List<SideJobItem> tasks = result.tasks();
        for (SideJobItem task : tasks) {
            SideJob sideJob = SideJob.builder()
                    .userId(request.userId())
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
