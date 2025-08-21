package com.booquest.booquest_api.adapter.in.mission.dto;


public record MissionGenerateRequestDto(
        long userId, //  나중에 rag 를 사용하기 위해서는 사용자 id 를 받는 것이 더 좋을 듯.
        Long sideJobId,
        String sideJobTitle,
        String sideJobDesignNotes
){ }