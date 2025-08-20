package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto.RegenerateAllSideJobRequest;
import com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto.RegenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.sidejob.GenerateSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.RegenerateSideJobUseCase;
import com.booquest.booquest_api.application.port.in.sidejob.SideJobGenerationResult;
import com.booquest.booquest_api.application.port.in.sidejob.SideJobItem;
import com.booquest.booquest_api.application.port.out.sidejob.GenerateSideJobPort;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenerateSideJobService implements GenerateSideJobUseCase, RegenerateSideJobUseCase {

    private final SideJobRepositoryPort sideJobRepository;

    private final GenerateSideJobPort sideJobGenerator;

    @Transactional
    @Override
    public List<SideJob> generateSideJob(GenerateSideJobRequest request) {
        SideJobGenerationResult result = sideJobGenerator.generateSideJobs(request);
        return saveSideJobs(request.userId(), result.tasks(), result.prompt(), null);
    }

    @Override
    public List<SideJob> regenerateAll(RegenerateAllSideJobRequest request) {
        List<SideJob> sideJobs = sideJobRepository.findAllByIds(request.sideJobIds());

        GenerateSideJobRequest generateSideJobRequest = request.generateSideJobRequest();
        SideJobGenerationResult result = sideJobGenerator.generateSideJobs(generateSideJobRequest);

        if (sideJobs.size() != result.tasks().size()) {
            throw new IllegalArgumentException("기존 부업 개수와 생성된 개수가 일치하지 않습니다.");
        }

        // ID 매핑
        List<Long> ids = sideJobs.stream().map(SideJob::getId).toList();
        return saveSideJobs(generateSideJobRequest.userId(), result.tasks(), result.prompt(), ids);
    }

    private List<SideJob> saveSideJobs(Long userId, List<SideJobItem> tasks, String promptMeta, List<Long> ids) {
        List<SideJob> savedJobs = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++) {
            SideJobItem task = tasks.get(i);

            SideJob.SideJobBuilder builder = SideJob.builder()
                    .userId(userId)
                    .title(task.title())
                    .description(task.description())
                    .promptMeta(promptMeta)
                    .isSelected(false);

            if (ids != null) {
                builder.id(ids.get(i)); // regenerate의 경우 id 주입
            }

            savedJobs.add(sideJobRepository.save(builder.build()));
        }

        return savedJobs;
    }

    @Override
    public SideJob regenerate(Long sideJobId, RegenerateSideJobRequest request) {
        SideJobGenerationResult result = sideJobGenerator.regenerateSideJob(request);
        SideJobItem sideJobItem = result.tasks().getFirst();

        SideJob sideJob = SideJob.builder()
                .id(sideJobId)
                .userId(request.generateSideJobRequest().userId())
                .title(sideJobItem.title())
                .description(sideJobItem.description())
                .promptMeta(result.prompt())
                .isSelected(false)
                .build();

        return sideJobRepository.save(sideJob);
    }
}
