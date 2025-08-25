package com.booquest.booquest_api.adapter.in.usersidejob.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSideJobProgressResponse {
    private Long sideJobId;
    private String title;
    private UserSideJobProgressDto progress;
}
