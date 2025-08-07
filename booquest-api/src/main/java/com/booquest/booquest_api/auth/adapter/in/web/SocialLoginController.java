package com.booquest.booquest_api.auth.adapter.in.web;

import com.booquest.booquest_api.auth.adapter.in.web.dto.SocialLoginRequest;
import com.booquest.booquest_api.auth.adapter.in.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.auth.application.port.in.SocialLoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SocialLoginController {

    private final SocialLoginUseCase socialLoginUseCase;

    @PostMapping("/login")
    public ResponseEntity<SocialLoginResponse> socialLogin(@RequestBody SocialLoginRequest request) {
        SocialLoginResponse response = socialLoginUseCase.login(request.accessToken());

        return ResponseEntity.ok(response);
    }
}
