package com.booquest.booquest_api.adapter.in.user;

import com.booquest.booquest_api.adapter.out.user.dto.UserResponse;
import com.booquest.booquest_api.application.port.in.user.GetUserUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final GetUserUseCase getUserUseCase;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getUserFromAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserResponse response = getUserUseCase.getUserFromAuth(auth);
        return ApiResponse.success("사용자 정보가 조회되었습니다.", response);
    }
}
