package com.booquest.booquest_api.application.port.in.sidejob.mission.missionstep;

import java.util.List;

public record GenerateMissionStepResult(
        boolean success,
        String message,
        List<MissionStepDto> steps
) { }