package com.booquest.booquest_api.adapter.out.sidejob.mission.missionstep.persisitence;

import com.booquest.booquest_api.application.port.out.sidejob.mission.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.MissionStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class MissionStepRepositoryAdapter implements MissionStepRepositoryPort {

    private final MissionStepRepository missionStepRepository;

    @Override
    public List<MissionStep> saveAll(Iterable<MissionStep> missionSteps) {
        return missionStepRepository.saveAll(missionSteps);
    }

    @Override
    public Optional<MissionStep> findById(Long id) {
        return missionStepRepository.findById(id);
    }
}