package com.booquest.booquest_api.application.port.in.sidejob;

import java.util.List;

public record SideJobGenerationResult(
        boolean success,
        String message,
        List<SideJobItem> tasks,
        String prompt
) { }

