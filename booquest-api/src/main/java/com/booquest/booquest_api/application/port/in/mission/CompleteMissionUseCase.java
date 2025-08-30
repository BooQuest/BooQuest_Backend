package com.booquest.booquest_api.application.port.in.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionCompleteResponse;

public interface CompleteMissionUseCase {
    /**
     * 메인퀘스트를 완료 처리하고 관련 경험치를 계산하여 반환
     */
    MissionCompleteResponse completeMission(Long missionId, Long userId);
} 