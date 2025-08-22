package com.booquest.booquest_api.application.service.income;

import com.booquest.booquest_api.adapter.in.income.web.dto.IncomeSummaryResponse;
import com.booquest.booquest_api.adapter.out.income.persistence.IncomeRepository;
import com.booquest.booquest_api.application.port.in.income.GetIncomeSummaryUseCase;
import com.booquest.booquest_api.adapter.out.usersidejob.persistence.UserSideJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetIncomeSummaryService implements GetIncomeSummaryUseCase {

    private final IncomeRepository incomeRepository;
    private final UserSideJobRepository userSideJobRepository;

    @Override
    public IncomeSummaryResponse getIncomeSummary(Long userId) {
//        // 총 수익 조회
//        Integer totalIncome = incomeRepository.sumAmountByUserId(userId);
//
//        // 완료된 부업 프로젝트 수 조회 (COMPLETED 상태)
//        Long completedSideJobCount = userSideJobRepository.countByUserIdAndStatus(userId, "COMPLETED");
//
//        // 완료된 퀘스트 수 조회 (mission_steps에서 completed 상태 카운트)
//        // TODO: MissionStepRepository에서 완료된 퀘스트 수 조회 로직 추가 필요
//
//        // 총 수익 등록 횟수 조회
//        Long totalIncomeCount = IncomeRepository.countByUserId(userId);
//
//        return IncomeSummaryResponse.builder()
//                .totalIncome(totalIncome != null ? totalIncome.longValue() : 0L)
//                .completedSideJobCount(completedSideJobCount != null ? completedSideJobCount : 0L)
//                .completedQuestCount(0L) // TODO: 실제 퀘스트 완료 수 조회 로직 구현 필요
//                .totalIncomeCount(totalIncomeCount != null ? totalIncomeCount : 0L)
//                .build();
        return null;
    }
} 