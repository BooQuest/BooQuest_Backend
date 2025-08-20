package com.booquest.booquest_api.application.service.sidejob;

import com.booquest.booquest_api.adapter.in.onboarding.web.sidejob.dto.RegenerateAllSideJobRequest;
import com.booquest.booquest_api.application.port.in.dto.GenerateSideJobRequest;
import com.booquest.booquest_api.application.port.in.sidejob.SideJobGenerationResult;
import com.booquest.booquest_api.application.port.in.sidejob.SideJobItem;
import com.booquest.booquest_api.application.port.out.sidejob.GenerateSideJobPort;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

class GenerateSideJobServiceTest {

    @Mock
    private SideJobRepositoryPort sideJobRepository;

    @Mock
    private GenerateSideJobPort sideJobGenerator;

    @InjectMocks
    private GenerateSideJobService generateSideJobService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("요청을 받아 부업 목록을 생성하고 저장한다")
    void generateSideJob_shouldGenerateAndSaveTasks() {
        // given
        GenerateSideJobRequest request = new GenerateSideJobRequest(1L, "개발자", List.of("독서"), "TEXT", "LOGIC");

        List<SideJobItem> items = List.of(
                new SideJobItem("부업1", "설명1"),
                new SideJobItem("부업2", "설명2")
        );
        SideJobGenerationResult result = new SideJobGenerationResult(true, "", items, "프롬프트");

        given(sideJobGenerator.generateSideJobs(request)).willReturn(result);

        given(sideJobRepository.save(any(SideJob.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        List<SideJob> saved = generateSideJobService.generateSideJob(request);

        // then
        assertThat(saved).hasSize(2);
        assertThat(saved.get(0).getTitle()).isEqualTo("부업1");
        assertThat(saved.get(1).getTitle()).isEqualTo("부업2");
    }

    @Test
    @DisplayName("전체 재생성시 요청을 받아 기존 부업 ID 순서대로 새로 생성하고 덮어쓴다")
    void regenerateAll_shouldRegenerateAndOverwrite() {
        // given
        List<Long> ids = List.of(10L, 20L);
        RegenerateAllSideJobRequest request = new RegenerateAllSideJobRequest(
                ids,
                new GenerateSideJobRequest(1L, "디자이너", List.of("그림"), "TEXT", "CREATIVE")
        );

        List<SideJob> existing = List.of(
                SideJob.builder().id(10L).build(),
                SideJob.builder().id(20L).build()
        );
        given(sideJobRepository.findAllByIds(ids)).willReturn(existing);

        List<SideJobItem> items = List.of(
                new SideJobItem("새부업1", "새설명1"),
                new SideJobItem("새부업2", "새설명2")
        );
        SideJobGenerationResult result = new SideJobGenerationResult(true, "", items, "새프롬프트");
        given(sideJobGenerator.generateSideJobs(request.generateSideJobRequest())).willReturn(result);

        given(sideJobRepository.save(any(SideJob.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        List<SideJob> regenerated = generateSideJobService.regenerateAll(request);

        // then
        assertThat(regenerated).hasSize(2);
        assertThat(regenerated.get(0).getId()).isEqualTo(10L);
        assertThat(regenerated.get(0).getTitle()).isEqualTo("새부업1");
        assertThat(regenerated.get(1).getId()).isEqualTo(20L);
        assertThat(regenerated.get(1).getTitle()).isEqualTo("새부업2");
    }

    @Test
    @DisplayName("전체 재생성시 기존 부업 개수와 새로 생성된 개수가 다르면 예외 발생")
    void regenerateAll_shouldThrowIfSizeMismatch() {
        // given
        List<Long> ids = List.of(10L);
        RegenerateAllSideJobRequest request = new RegenerateAllSideJobRequest(
                ids,
                new GenerateSideJobRequest(1L, "작가", List.of("글쓰기"), "TEXT", "CREATIVE")
        );

        given(sideJobRepository.findAllByIds(ids))
                .willReturn(List.of(SideJob.builder().id(10L).build()));

        List<SideJobItem> mismatchItems = List.of(
                new SideJobItem("하나", "하나 설명"),
                new SideJobItem("둘", "둘 설명") // 더 많음
        );
        SideJobGenerationResult mismatchResult = new SideJobGenerationResult(true, "", mismatchItems, "새프롬프트");

        given(sideJobGenerator.generateSideJobs(request.generateSideJobRequest()))
                .willReturn(mismatchResult);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> generateSideJobService.regenerateAll(request));
    }
}
