package com.booquest.booquest_api.application.dto;

public record SideJobGenerationResult(
        String title,
        String description,
        String prompt
) { }
