package com.booquest.booquest_api.application.port.in.sidejob.mission.missionstep;

public record MissionStepDto(
        String title,
        int seq,
        String detail
) { }