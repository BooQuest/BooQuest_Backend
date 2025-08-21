package com.booquest.booquest_api.application.port.in.mission;

import java.util.List;

public record GenerateMissionResult(
        boolean success,
        String message,
        List<MissionDraft> tasks
) { }