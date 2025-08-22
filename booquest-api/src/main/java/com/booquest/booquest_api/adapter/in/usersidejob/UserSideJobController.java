package com.booquest.booquest_api.adapter.in.usersidejob;

import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobCreateRequest;
import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobListResponse;
import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobResponse;
import com.booquest.booquest_api.adapter.in.usersidejob.dto.UserSideJobSummaryResponse;
import com.booquest.booquest_api.application.port.in.usersidejob.CreateUserSideJobUseCase;
import com.booquest.booquest_api.application.port.in.usersidejob.GetUserSideJobListUseCase;
import com.booquest.booquest_api.application.port.in.usersidejob.GetUserSideJobSummaryUseCase;
import com.booquest.booquest_api.application.port.in.usersidejob.GetUserSideJobUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/sideJob")
@Tag(name = "User Side Job", description = "사용자의 부업 API")
public class UserSideJobController {
    private final CreateUserSideJobUseCase createUserSideJobUseCase;
    private final GetUserSideJobListUseCase getSideJobListUserCase;
    private final GetUserSideJobUseCase getSideJobUserCase;
    private final GetUserSideJobSummaryUseCase getUserSideJobSummaryUseCase;

    @PostMapping
    @Operation(summary = "부업 시작 (사용자 부업 생성)", description = "추천받은 부업 중 하나를 선택해, 로그인한 사용자의 부업으로 생성합니다. </br>" +
            "이미 진행중인 부업일 경우 새로 생성되지 않습니다. </br></br>응답값 data.result: created (새로 생성됨), already_exists (이미 진행중인 부업 반환)")
    public ApiResponse<UserSideJobResponse> createUserSideJob(@Valid @RequestBody UserSideJobCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        UserSideJobResponse response = createUserSideJobUseCase.createUserSideJob(userId, request.getSideJobId());
        Boolean exists = response.getExists();
        String message = Boolean.TRUE.equals(exists) ? "이미 진행 중인 부업입니다." : "부업을 시작했습니다.";
        return ApiResponse.success(message, response);
    }

    @GetMapping
    @Operation(summary = "부업 목록 조회", description = "로그인한 사용자의 부업 목록을 조회합니다.")
    public ApiResponse<List<UserSideJobListResponse>> getUserSideJobList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        List<UserSideJobListResponse> response = getSideJobListUserCase.getUserSideJobList(userId);
        return ApiResponse.success("부업 목록을 조회하였습니다.", response);
    }

    @GetMapping("/{userSideJobId}")
    @Operation(summary = "부업 조회", description = "로그인한 사용자의 부업을 조회합니다.")
    public ApiResponse<UserSideJobResponse> getUserSideJob(@PathVariable Long userSideJobId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(auth.getName());

        UserSideJobResponse response = getSideJobUserCase.getUserSideJob(userId, userSideJobId);
        return ApiResponse.success("부업을 조회하였습니다.", response);
    }

//    @GetMapping("/{userSideJobId}/summary")
//    @Operation(summary = "부업 요약 조회", description = "로그인한 사용자의 부업 요약을 조회합니다.")
//    public ApiResponse<UserSideJobSummaryResponse> getUserSideJobSummary(@PathVariable Long userSideJobId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.parseLong(auth.getName());
//
//        UserSideJobSummaryResponse response = getUserSideJobSummaryUseCase.getUserSideJobSummary(userId, userSideJobId);
//        return ApiResponse.success("부업 요약을 조회하였습니다.", response);
//    }
}
