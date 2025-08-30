package com.booquest.booquest_api.adapter.out.mission.persisitence;

import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MissionRepositoryAdapter implements MissionRepositoryPort {

    private final MissionRepository missionRepository;

    @Override
    public Mission save(Mission mission) {
        return missionRepository.save(mission);
    }

    @Override
    public List<Mission> saveAll(Iterable<Mission> missions) {
        return missionRepository.saveAll(missions);
    }

    @Override
    public Optional<Mission> findByIdWithSteps(Long missionId){
        return missionRepository.findByIdWithSteps(missionId);
    }

    @Override
    public boolean isExistByUserId(Long userId) {
        return missionRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsByUserIdAndStatusNot(Long userId, MissionStatus missionStatus) {
        return missionRepository.existsByUserIdAndStatusNot(userId, missionStatus);
    }
    
    @Override
    public List<Mission> findByUserIdAndSideJobIdOrderByOrderNo(Long userId, Long sideJobId) {
        return missionRepository.findByUserIdAndSideJobIdOrderByOrderNo(userId, sideJobId);
    }
    
    @Override
    public Optional<Mission> findById(Long missionId) {
        return missionRepository.findById(missionId);
    }

    @Override
    public List<Mission> findByUserId(Long userId) {
        return missionRepository.findByUserIdWithSteps(userId);
    }

    @Override
    public List<Mission> findByUserIdAndStatus(Long userId, MissionStatus status) {
        return missionRepository.findByUserIdAndStatusWithSteps(userId, status);
    }

    @Override
    public List<Mission> findByUserIdAndSideJobId(long userId, long sideJobId) {
        return missionRepository.findByUserIdAndSideJobId(userId, sideJobId);
    }

    @Override
    public List<Mission> findListWithOptionalFilters(Long userId, Long sideJobId, MissionStatus status) {
        return missionRepository.findListWithOptionalFilters(userId, sideJobId, status);
    }

    @Override
    public long deleteByUserId(Long userId) {
        return (long) missionRepository.deleteByUserId(userId);
    }
}
