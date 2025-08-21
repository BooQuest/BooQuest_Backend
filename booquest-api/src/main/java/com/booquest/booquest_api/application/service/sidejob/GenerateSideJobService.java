package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.adapter.in.sidejob.dto.RegenerateAllSideJobRequest;
import com.booquest.booquest_api.adapter.in.sidejob.dto.RegenerateSideJobRequest;
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
        return saveGeneratedJobs(request.userId(), result, null);
    }

    @Transactional
    @Override
    public List<SideJob> regenerateAll(RegenerateAllSideJobRequest request) {
        List<SideJob> existingJobs = sideJobRepository.findAllByIds(request.sideJobIds());
        SideJobGenerationResult result = sideJobGenerator.generateSideJobs(request.generateSideJobRequest());

        if (existingJobs.size() != result.tasks().size()) {
            throw new IllegalArgumentException("기존 부업 개수와 생성된 개수가 일치하지 않습니다.");
        }

        List<Long> ids = existingJobs.stream().map(SideJob::getId).toList();
        return saveGeneratedJobs(request.generateSideJobRequest().userId(), result, ids);
    }

    @Transactional
    @Override
    public SideJob regenerate(Long sideJobId, RegenerateSideJobRequest request) {
        SideJobGenerationResult result = sideJobGenerator.regenerateSideJob(request);
        return saveGeneratedJob(request.generateSideJobRequest().userId(), result.tasks().getFirst(), result.prompt(), sideJobId);
    }

    // 공통 저장 로직
    private List<SideJob> saveGeneratedJobs(Long userId, SideJobGenerationResult result, List<Long> ids) {
        List<SideJob> saved = new ArrayList<>();
        List<SideJobItem> tasks = result.tasks();

        for (int i = 0; i < tasks.size(); i++) {
            Long id = (ids != null) ? ids.get(i) : null;
            saved.add(saveGeneratedJob(userId, tasks.get(i), result.prompt(), id));
        }

        return saved;
    }

    private SideJob saveGeneratedJob(Long userId, SideJobItem task, String promptMeta, Long id) {
        SideJob.SideJobBuilder builder = SideJob.builder()
                .userId(userId)
                .title(task.title())
                .description(task.description())
                .promptMeta(promptMeta)
                .isSelected(false);

        if (id != null) builder.id(id);

        return sideJobRepository.save(builder.build());
    }
}
