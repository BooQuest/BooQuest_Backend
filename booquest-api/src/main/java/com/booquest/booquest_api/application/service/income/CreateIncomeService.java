package com.booquest.booquest_api.application.service.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.CreateIncomeRequest;
import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeResponse;
import com.booquest.booquest_api.application.port.in.income.CreateIncomeUseCase;
import com.booquest.booquest_api.domain.user.model.User;
import com.booquest.booquest_api.adapter.out.auth.persistence.jpa.UserRepository;
import com.booquest.booquest_api.domain.income.model.Income;
import com.booquest.booquest_api.adapter.out.income.persistence.IncomeRepository;
import com.booquest.booquest_api.domain.usersidejob.model.UserSideJob;
import com.booquest.booquest_api.adapter.out.usersidejob.persistence.UserSideJobRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateIncomeService implements CreateIncomeUseCase {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final UserSideJobRepository userSideJobRepository;

    @Override
    public IncomeResponse createIncome(Long userId, CreateIncomeRequest request) {
        validateRequest(request);

        User user = getUser(userId);
        UserSideJob userSideJob = getUserSideJobAndCheckUserPermission(userId, request.getUserSideJobId());

        Income income = buildIncome(user, userSideJob, request);
        Income createdIncome = create(income);

        return IncomeResponse.toResponse(createdIncome);
    }

    private void validateRequest(CreateIncomeRequest request) {
        if (request.getUserSideJobId() == null) {
            throw new IllegalArgumentException("userSideJobId is required");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        if (request.getAmount() == null || request.getAmount() < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }
        if (request.getIncomeDate() == null) {
            throw new IllegalArgumentException("incomeDate is required");
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    private UserSideJob getUserSideJobAndCheckUserPermission(Long userId, Long userSideJobId) {
        UserSideJob userSideJob = userSideJobRepository.findById(userSideJobId)
                .orElseThrow(() -> new EntityNotFoundException("UserSideJob not found: " + userSideJobId));
        if (!userSideJob.getUserId().equals(userId)) {
            throw new IllegalArgumentException("No permission to create income for this side job");
        }
        return userSideJob;
    }

    private Income buildIncome(User user, UserSideJob userSideJob, CreateIncomeRequest request) {
        return Income.builder()
                .user(user)
                .userSideJobId(userSideJob.getId())
                .title(request.getTitle())
                .amount(request.getAmount())
                .incomeDate(request.getIncomeDate())
                .memo(request.getMemo())
                .build();
    }

    private Income create(Income income) {
        return incomeRepository.save(income);
    }
}
