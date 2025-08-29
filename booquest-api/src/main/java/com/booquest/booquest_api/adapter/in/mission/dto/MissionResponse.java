package com.booquest.booquest_api.adapter.in.mission.dto;

import com.booquest.booquest_api.adapter.in.missionstep.dto.MissionStepResponse;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.booquest.booquest_api.domain.missionstep.model.MissionStep;
import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class MissionResponse {
    private Long id;
    private Long sideJobId;
    private String title;
    private String status;
    private int orderNo;
    private String designNotes;
    private String guide;
    private int missionTotalExp;
    private List<MissionStepResponse> steps;

    private MissionStepProgressResponse progress;

    public static MissionResponse toResponse(Mission mission, int missionTotalExp) {
        List<MissionStepResponse> stepResponses = mission.getSteps().stream()
                .map(MissionStepResponse::toResponse)
                .collect(Collectors.toList());

        MissionStepProgressResponse progress = calculateProgress(mission);

        return MissionResponse.builder()
                .id(mission.getId())
                .sideJobId(mission.getSideJob().getId())
                .title(mission.getTitle())
                .status(mission.getStatus() != null ? mission.getStatus().name() : null)
                .orderNo(mission.getOrderNo())
                .designNotes(mission.getDesignNotes())
                .guide(mission.getGuide())
                .missionTotalExp(missionTotalExp)
                .steps(stepResponses)
                .progress(progress)
                .build();
    }

    private static MissionStepProgressResponse calculateProgress(Mission mission) {
        Set<MissionStep> steps = mission.getSteps();

        int total = steps.size();
        long done = steps.stream().filter(MissionStep::isCompleted).count();
        int percent = (total == 0) ? 0 : (int) Math.round(done * 100.0 / total);

        MissionStep current = steps.stream()
                .filter(s -> !s.isCompleted())
                .min(Comparator.comparingInt(MissionStep::getSeq))
                .orElse(null);

        return MissionStepProgressResponse.builder()
                .percent(percent)
                .completedStepCount((int) done)
                .totalStepCount(total)
                .currentStepId(current == null ? null : current.getId())
                .currentStepOrder(current == null ? null : current.getSeq())
                .build();
    }
}
