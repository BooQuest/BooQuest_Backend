package com.booquest.booquest_api.auth.adapter.in.web;

import com.booquest.booquest_api.auth.application.port.in.SocialLoginUseCase;
import com.booquest.booquest_api.auth.domain.model.SocialUser;
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

    @PostMapping("/social-login")
    public ResponseEntity<SocialUser> socialLogin(@RequestBody String token) {
        SocialUser user = socialLoginUseCase.login(token);
        return ResponseEntity.ok(user);
    }
}
