package com.booquest.booquest_api.application.port.out.version;

import com.booquest.booquest_api.adapter.in.version.web.dto.LatestAppVersionResponseDto;
import com.booquest.booquest_api.domain.version.AppPlatform;

public interface AppVersionRepositoryPort {
    LatestAppVersionResponseDto findLatestByPlatform(AppPlatform platform);
}
