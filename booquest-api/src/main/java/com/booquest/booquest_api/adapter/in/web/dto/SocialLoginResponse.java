package com.booquest.booquest_api.adapter.in.web.dto;

import com.booquest.booquest_api.domain.user.model.SocialUser;

public record SocialLoginResponse(
        boolean registered,
        String token, // 회원이면 포함
        SocialUser userInfo // 둘 다 포함
) {}
