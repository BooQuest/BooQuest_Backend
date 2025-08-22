package com.booquest.booquest_api.adapter.in.income.web;

import com.booquest.booquest_api.adapter.in.income.web.dto.*;
import com.booquest.booquest_api.application.port.in.income.*;
import com.booquest.booquest_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/income")
@Tag(name = "Income", description = "수익 API")
public class IncomeController {

    private final GetIncomeSummaryUseCase getIncomeSummaryUseCase;
    private final GetIncomeListUseCase getIncomeListUseCase;
    private final GetIncomeUseCase getIncomeUseCase;
    private final CreateIncomeUseCase createIncomeUseCase;
    private final UpdateIncomeUseCase updateIncomeUseCase;
    private final DeleteIncomeUseCase deleteIncomeUseCase;

//    @GetMapping("/summary")
//    @Operation(summary = "수익 요약 조회", description = "로그인한 사용자의 수익 요약을 조회합니다.")
//    public ApiResponse<IncomeSummaryResponse> getIncomeSummary() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.parseLong(auth.getName());
//
//        IncomeSummaryResponse response = getIncomeSummaryUseCase.getIncomeSummary(userId);
//        return ApiResponse.success("", response);
//    }

    @GetMapping
    @Operation(summary = "수익 목록 조회", description = "로그인한 사용자의 수익 목록을 조회합니다.")
    public ApiResponse<IncomeListResponse> getIncomeList(@RequestParam(required = false) Long userSideJobId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        IncomeListResponse response = getIncomeListUseCase.getIncomeList(userId, userSideJobId);
        return ApiResponse.success("수익 목록이 조회되었습니다.", response);
    }

    @GetMapping("/{incomeId}")
    @Operation(summary = "수익 조회", description = "로그인한 사용자의 수익을 조회합니다.")
    public ApiResponse<IncomeResponse> getIncome(@PathVariable Long incomeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        IncomeResponse response = getIncomeUseCase.getIncome(userId, incomeId);
        return ApiResponse.success("수익이 조회되었습니다.", response);
    }

    @PostMapping
    @Operation(summary = "수익 생성", description = "로그인한 사용자의 수익을 생성합니다.")
    public ApiResponse<IncomeResponse> createIncome(@Valid @RequestBody CreateIncomeRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        IncomeResponse response = createIncomeUseCase.createIncome(userId, request);
        return ApiResponse.success("수익이 생성되었습니다.", response);
    }

    @PutMapping("/{incomeId}")
    @Operation(summary = "수익 수정", description = "로그인한 사용자의 수익을 수정합니다.")
    public ApiResponse<IncomeResponse> updateIncome(@PathVariable Long incomeId, @Valid @RequestBody UpdateIncomeRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        IncomeResponse response = updateIncomeUseCase.updateIncome(userId, incomeId, request);
        return ApiResponse.success("수익이 수정되었습니다.", response);
    }

    @DeleteMapping("/{incomeId}")
    @Operation(summary = "수익 삭제", description = "로그인한 사용자의 수익을 삭제합니다.")
    public ApiResponse<Void> deleteIncome(@PathVariable Long incomeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        deleteIncomeUseCase.deleteIncome(userId, incomeId);
        return ApiResponse.success("수익이 삭제되었습니다.", null);
    }
}
