package com.booquest.booquest_api.adapter.in.usersidejob.dto;

import com.booquest.booquest_api.domain.usersidejob.enums.UserSideJobStatus;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserSideJobResponse {
    private Long id;
    private Long sideJobId;
    private String title;
    private String description;
    private UserSideJobStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public static UserSideJobResponse toResponse(UserSideJob userSideJob) {
        return UserSideJobResponse.builder()
                .id(userSideJob.getId())
                .sideJobId(userSideJob.getSideJobId())
                .title(userSideJob.getTitle())
                .description(userSideJob.getDescription())
                .status(userSideJob.getStatus())
                .startedAt(userSideJob.getStartedAt())
                .endedAt(userSideJob.getEndedAt())
                .build();
    }
}
