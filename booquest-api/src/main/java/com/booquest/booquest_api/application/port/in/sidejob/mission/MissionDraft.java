package com.booquest.booquest_api.application.port.in.sidejob.mission;

public record MissionDraft(
        String title,
        int orderNo,
        String notes
) { }