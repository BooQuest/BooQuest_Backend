package com.booquest.booquest_api.adapter.out.activity;

import com.booquest.booquest_api.adapter.out.missionstep.persisitence.MissionStepRepository;
import com.booquest.booquest_api.application.port.out.activity.MissionStepQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MissionStepQueryAdapter implements MissionStepQueryPort {
    private final MissionStepRepository missionStepRepository;

    @Override
    public int countCompletedStepsByUserId(Long userId) {
        return (int) missionStepRepository.countCompletedStepsByUserId(userId);
    }
}
