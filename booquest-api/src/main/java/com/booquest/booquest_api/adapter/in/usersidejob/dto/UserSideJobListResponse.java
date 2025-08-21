package com.booquest.booquest_api.adapter.in.usersidejob.dto;

import com.booquest.booquest_api.domain.usersidejob.enums.UserSideJobStatus;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class UserSideJobListResponse {
    private Long id;
    private Long sideJobId;
    private String title;
    private String description;
    private UserSideJobStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String period;

    private static final DateTimeFormatter D = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static UserSideJobListResponse toResponse(UserSideJob userSideJob) {
        UserSideJobStatus status = getStatus(userSideJob);
        String period = calculatePeriod(userSideJob.getStartedAt(), userSideJob.getEndedAt());

        return UserSideJobListResponse.builder()
                .id(userSideJob.getId())
                .sideJobId(userSideJob.getSideJobId())
                .title(userSideJob.getTitle())
                .description(userSideJob.getDescription())
                .status(status)
                .startedAt(userSideJob.getStartedAt())
                .endedAt(userSideJob.getEndedAt())
                .period(period)
                .build();
    }

    private static UserSideJobStatus getStatus(UserSideJob userSideJob) {
        if (userSideJob.getStatus() != null) return userSideJob.getStatus();
        if (userSideJob.getEndedAt() != null) return UserSideJobStatus.COMPLETED;
        if (userSideJob.getStartedAt() != null) return UserSideJobStatus.IN_PROGRESS;
        return UserSideJobStatus.PLANNED;
    }

    private static String calculatePeriod(LocalDateTime startedAt, LocalDateTime endedAt) {
        // yyyy.MM.dd ~ yyyy.MM.dd / yyyy.MM.dd ~ 진행중 / 시작일 없으면 "-"
        if (startedAt == null) return "-";
        String start = D.format(startedAt);
        if (endedAt != null)  return start + " ~ " + D.format(endedAt);
        return start + " ~ 진행중";
    }
}