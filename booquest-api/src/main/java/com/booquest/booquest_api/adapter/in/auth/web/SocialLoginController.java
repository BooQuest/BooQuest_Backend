package com.booquest.booquest_api.adapter.in.auth.web;

import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginRequest;
import com.booquest.booquest_api.adapter.in.auth.web.dto.SocialLoginResponse;
import com.booquest.booquest_api.application.port.in.auth.SocialLoginUseCase;
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
        SocialLoginResponse response = socialLoginUseCase.login(request.getAccessToken(), request.getProvider());
        return ResponseEntity.ok(response);
    }
}
