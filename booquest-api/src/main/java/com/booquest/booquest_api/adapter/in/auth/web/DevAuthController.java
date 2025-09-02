package com.booquest.booquest_api.adapter.in.auth.web;

import com.booquest.booquest_api.application.port.in.auth.TokenUseCase;
import com.booquest.booquest_api.adapter.in.auth.web.token.dto.TokenInfo;
import com.booquest.booquest_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Profile({"local","dev"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dev")
@Tag(name = "Dev", description = "개발용 API")
public class DevAuthController {

    private final TokenUseCase tokenUseCase;

    @PostMapping("/test-token")
    @Operation(summary = "테스트용 토큰 발급", description = "테스트용 토큰을 발급합니다.")
    public ApiResponse<TokenInfo> issueTestToken(@RequestParam Long userId) {
        TokenInfo response = tokenUseCase.issueTestToken(userId);
        return ApiResponse.success("테스트용 토큰이 발급되었습니다.", response);
    }
}
