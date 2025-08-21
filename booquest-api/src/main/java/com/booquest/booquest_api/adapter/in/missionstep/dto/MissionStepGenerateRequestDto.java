package com.booquest.booquest_api.adapter.in.missionstep.dto;


public record MissionStepGenerateRequestDto(
        long userId, // rag 를 위해서는 남겨둬보자
        Long missionId,
        String missionTitle,
        String missionDesignNotes
){ }