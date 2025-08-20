package com.booquest.booquest_api.adapter.in.auth.web;

import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenInfo;
import com.booquest.booquest_api.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile({"local","dev"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/test")
public class DevAuthController {

    private final TokenUseCase tokenUseCase;

    @PostMapping("/token")
    public ApiResponse<TokenInfo> issueTestToken() {
        TokenInfo response = tokenUseCase.issueTestToken();
        return ApiResponse.success("테스트용 토큰이 발급되었습니다.", response);
    }
}
