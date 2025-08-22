package com.booquest.booquest_api.application.service.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobSummaryResponse;
import com.booquest.booquest_api.application.port.in.usersidejob.GetUserSideJobSummaryUseCase;
import com.booquest.booquest_api.application.port.out.usersidejob.UserSideJobRepositoryPort;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class GetUserSideJobSummaryService implements GetUserSideJobSummaryUseCase {
    private final UserSideJobRepositoryPort userSideJobRepositoryPort;

    @Override
    public UserSideJobSummaryResponse getUserSideJobSummary(Long userId, Long userSideJobId) {
        UserSideJob userSideJob = userSideJobRepositoryPort.findById(userSideJobId)
                .filter(e -> e.getUserId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("UserSideJob not found: " + userSideJobId));

        String category = "";
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int completedQuestCount = 0;
        int daysToFirstRevenue = 0;

        return UserSideJobSummaryResponse.toResponse(userSideJob, category, totalRevenue, completedQuestCount, daysToFirstRevenue);
    }
}
