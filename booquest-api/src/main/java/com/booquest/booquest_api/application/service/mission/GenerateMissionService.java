package com.booquest.booquest_api.application.service.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.mission.GenerateMissionResult;
import com.booquest.booquest_api.application.port.in.mission.GenerateMissionUseCase;
import com.booquest.booquest_api.application.port.out.mission.GenerateMissionPort;
import com.booquest.booquest_api.application.port.out.mission.MissionRepositoryPort;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.domain.mission.enums.MissionStatus;
import com.booquest.booquest_api.domain.mission.model.Mission;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GenerateMissionService implements GenerateMissionUseCase {

    private final SideJobRepositoryPort sideJobRepository;
     private final MissionRepositoryPort missionRepository;
    private final GenerateMissionPort missionGenerator;

    @Transactional
    @Override
    public List<Mission> generateMission(MissionGenerateRequestDto generateDto) {
        var sideJobSelectedId = generateDto.sideJobId();
        var userId = generateDto.userId();

        // side job is_select false 로 update
        sideJobRepository.updateSelectedTrue(sideJobSelectedId);

        //  ai 서버로 미션생성 요청
        GenerateMissionResult result = missionGenerator.generateMission(generateDto);

        List<Mission> existing = missionRepository.findByUserIdAndSideJobId(userId, sideJobSelectedId);

        Map<Integer, Mission> existingByOrder = existing.stream()
                .collect(Collectors.toMap(Mission::getOrderNo, m -> m));

        List<Mission> missions = result.tasks().stream()
                .map(t -> {
                    Mission existingMission = existingByOrder.get(t.orderNo());
                    if (existingMission != null) {
                        // 업데이트
                        existingMission.updateTitleAndNotes(t.title(), t.notes());
                        return existingMission;
                    } else {
                        // 새로 추가
                        return Mission.builder()
                                .sideJobId(sideJobSelectedId)
                                .userId(userId)
                                .title(t.title())
                                .status(MissionStatus.PLANNED)
                                .orderNo(t.orderNo())
                                .designNotes(t.notes())
                                .build();
                    }
                }).toList();

        return missionRepository.saveAll(missions);
    }
}
