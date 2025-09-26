package com.booquest.booquest_api.application.port.in.version;


import com.booquest.booquest_api.domain.version.AppPlatform;
import com.booquest.booquest_api.adapter.in.version.web.dto.LatestAppVersionResponseDto;

public interface CheckAppVersionUseCase {
    LatestAppVersionResponseDto getLatestVersion(AppPlatform platform);
}
