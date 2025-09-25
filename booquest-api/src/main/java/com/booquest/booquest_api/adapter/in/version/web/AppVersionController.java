package com.booquest.booquest_api.adapter.in.version.web;

import com.booquest.booquest_api.adapter.in.version.web.dto.LatestAppVersionResponseDto;
import com.booquest.booquest_api.application.port.in.version.CheckAppVersionUseCase;
import com.booquest.booquest_api.common.response.ApiResponse;
import com.booquest.booquest_api.domain.version.AppPlatform;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version")
@Tag(name = "Version", description = "앱 버전 관련 API")
public class AppVersionController {

    private final CheckAppVersionUseCase checkAppVersionUseCase;

    @GetMapping
    public ApiResponse<LatestAppVersionResponseDto> checkCurrentAppVersion(@RequestParam AppPlatform platform) {

        LatestAppVersionResponseDto result = checkAppVersionUseCase.getLatestVersion(platform);

        return ApiResponse.success(result);
    }
}
