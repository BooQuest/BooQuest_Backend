package com.booquest.booquest_api.application.service.user;

import com.booquest.booquest_api.adapter.in.user.web.dto.MyActivitiesSummaryResponse;
import com.booquest.booquest_api.application.port.in.user.GetMyActivitiesSummaryUseCase;
import com.booquest.booquest_api.application.port.out.activity.IncomeQueryPort;
import com.booquest.booquest_api.application.port.out.activity.MissionStepQueryPort;
import com.booquest.booquest_api.application.port.out.activity.UserSideJobQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMyActivitiesSummaryService implements GetMyActivitiesSummaryUseCase {
    private final IncomeQueryPort incomeQueryPort;
    private final MissionStepQueryPort missionStepQueryPort;
    private final UserSideJobQueryPort userSideJobQueryPort;

    @Override
    public MyActivitiesSummaryResponse getSummary(Long userId) {
        long totalIncome = safeLong(incomeQueryPort.sumIncomeByUserId(userId));
        int completedSteps = missionStepQueryPort.countCompletedStepsByUserId(userId);
        int completedSideJobs = userSideJobQueryPort.countCompletedSideJobsByUserId(userId);

        return MyActivitiesSummaryResponse.builder()
                .totalIncome(totalIncome)
                .completedQuestCount(completedSteps)
                .completedSideJobCount(completedSideJobs)
                .build();
    }

    private long safeLong(Long v) { return v == null ? 0L : v; }
}
