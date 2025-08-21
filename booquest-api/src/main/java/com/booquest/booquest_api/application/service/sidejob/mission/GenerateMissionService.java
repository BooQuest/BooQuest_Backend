package com.booquest.booquest_api.application.service.sidejob.mission;

import com.booquest.booquest_api.adapter.in.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.mission.GenerateMissionResult;
import com.booquest.booquest_api.application.port.in.mission.GenerateMissionUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.application.port.out.sidejob.mission.GenerateMissionPort;
import com.booquest.booquest_api.application.port.out.sidejob.mission.MissionRepositoryPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.enums.MissionStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateMissionService implements GenerateMissionUseCase {

    private final SideJobRepositoryPort sideJobRepository;
     private final MissionRepositoryPort missionRepository;
    private final GenerateMissionPort missionGenerator;

    @Transactional
    @Override
    public List<Mission> generateMission(MissionGenerateRequestDto generateDto) {
        ObjectMapper mapper = new ObjectMapper();
        var sideJobSelectedId = generateDto.sideJobId();
        var userId = generateDto.userId();

        // side job is_select false 로 update
        sideJobRepository.updateSelectedTrue(sideJobSelectedId);

        //  ai 서버로 미션생성 요청
        GenerateMissionResult result = missionGenerator.generateMission(generateDto);

        var missions = result.tasks().stream()
                .map(t -> {
                    return Mission.builder()
                            .sideJobId(sideJobSelectedId)
                            .userId(userId)
                            .title(t.title())
                            .status(MissionStatus.PLANNED)
                            .orderNo(t.orderNo())
                            .designNotes(t.notes())
                            .build();
                }).toList();

         return missionRepository.saveAll(missions);
    }
}
