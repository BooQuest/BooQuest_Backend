package com.booquest.booquest_api.adapter.out.sidejob.mission.persisitence;

import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.application.port.out.sidejob.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.model.SideJob;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class MissionRepositoryAdapter implements MissionRepositoryPort {

    private final MissionRepository missionRepository;

    @Override
    public List<Mission> saveAll(Iterable<Mission> missions) {
        return missionRepository.saveAll(missions);
    }

    @Override
    public Optional<Mission> findByIdWithSteps(Long missionId){
        return missionRepository.findByIdWithSteps(missionId);
    }
}