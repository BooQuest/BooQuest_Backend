package com.booquest.booquest_api.application.service.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobSummaryResponse;
import com.booquest.booquest_api.application.port.in.usersidejob.GetUserSideJobSummaryUseCase;
import com.booquest.booquest_api.application.port.out.income.IncomeRepositoryPort;
import com.booquest.booquest_api.application.port.out.missionstep.MissionStepRepositoryPort;
import com.booquest.booquest_api.application.port.out.usersidejob.UserSideJobRepositoryPort;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GetUserSideJobSummaryService implements GetUserSideJobSummaryUseCase {
    private final UserSideJobRepositoryPort userSideJobRepositoryPort;
    private final IncomeRepositoryPort incomeRepositoryPort;
    private final MissionStepRepositoryPort missionStepRepositoryPort;

    @Override
    public UserSideJobSummaryResponse getUserSideJobSummary(Long userId, Long userSideJobId) {
        UserSideJob userSideJob = userSideJobRepositoryPort.findById(userSideJobId)
                .filter(e -> e.getUserId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("UserSideJob not found: " + userSideJobId));

        BigDecimal totalIncome = incomeRepositoryPort.sumByUserAndSideJob(userId, userSideJobId);

        int completedQuestCount = missionStepRepositoryPort.countCompletedByUserAndSideJob(userId, userSideJob.getSideJobId());

        int daysToFirstIncome = 0;
        LocalDate startedDate = null;
        if (userSideJob.getStartedAt() != null) {
            startedDate = userSideJob.getStartedAt().toLocalDate();
        }

        LocalDate firstIncomeDate = incomeRepositoryPort.findFirstIncomeDate(userId, userSideJobId);

        if (startedDate != null && firstIncomeDate != null) {
            long diff = firstIncomeDate.toEpochDay() - startedDate.toEpochDay(); // ChronoUnit 없이 '일수' 차이
            daysToFirstIncome = (int) Math.max(diff + 1, 1);    // 같은 날을 1일로 보려면: daysToFirstIncome += 1;
        }

        return UserSideJobSummaryResponse.toResponse(userSideJob, totalIncome, completedQuestCount, daysToFirstIncome);
    }
}
