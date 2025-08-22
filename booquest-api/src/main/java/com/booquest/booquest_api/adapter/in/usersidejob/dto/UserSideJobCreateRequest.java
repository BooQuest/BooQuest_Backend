package com.booquest.booquest_api.adapter.in.usersidejob.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSideJobCreateRequest {
    @NotNull
    private Long sideJobId;
//    private String titleOverride; // 제목 수정해서 시작하고 싶을 때(옵션)
}
