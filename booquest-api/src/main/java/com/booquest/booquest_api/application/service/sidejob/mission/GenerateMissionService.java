package com.booquest.booquest_api.application.service.sidejob.mission;

import com.booquest.booquest_api.adapter.in.onboarding.web.mission.dto.MissionGenerateRequestDto;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GenerateMissionResult;
import com.booquest.booquest_api.application.port.in.sidejob.mission.GenerateMissionUseCase;
import com.booquest.booquest_api.application.port.out.sidejob.SideJobRepositoryPort;
import com.booquest.booquest_api.application.port.out.sidejob.mission.GenerateMissionPort;
import com.booquest.booquest_api.domain.mission.model.Mission;
import com.booquest.booquest_api.domain.sidejob.enums.MissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateMissionService implements GenerateMissionUseCase {

    private final SideJobRepositoryPort sideJobRepository;
    // private final MissionRepository missionRepository; // 아직은 사용 X
    private final GenerateMissionPort missionGenerator;

    @Transactional
    @Override
    public List<Mission> generateMission(MissionGenerateRequestDto generateDto) {

        var sideJobSelectedId = generateDto.sideJobId();
        var userId = generateDto.userId();

        // side job is_select false 로 update
        sideJobRepository.updateSelectedFalse(sideJobSelectedId);

        //  ai 서버로 미션생성 요청
        GenerateMissionResult result = missionGenerator.generateMission(generateDto);

        return result.tasks().stream()
                .map(t -> Mission.builder()
                        .sidejobId(sideJobSelectedId)
                        .userId(userId)
                        .title(t.title())
                        .status(MissionStatus.PLANNED)
                        .orderNo(t.orderNo())
                        .designNotes(t.notes())
                        .build())
                .toList();

        // 미션 저장 후 반환 [to-do]
        // return missionRepository.saveAll(missions);
    }
}
