package com.booquest.booquest_api.adapter.in.mission.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MissionListResponse {
    private List<MissionResponse> missions;
}