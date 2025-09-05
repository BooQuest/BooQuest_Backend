package com.booquest.booquest_api.adapter.in.missionstep.dto;

public record MissionStepAiRequestDto(
        long userId,
        Long missionId,
        String missionTitle,
        String missionDesignNotes,
        int orderNo,
        String sideJobTitle,
        String sideJobDescription)
{}
