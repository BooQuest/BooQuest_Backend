package com.booquest.booquest_api.application.service.version;

import com.booquest.booquest_api.application.port.out.version.AppVersionRepositoryPort;
import com.booquest.booquest_api.domain.version.AppPlatform;
import com.booquest.booquest_api.adapter.in.version.web.dto.LatestAppVersionResponseDto;
import com.booquest.booquest_api.application.port.in.version.CheckAppVersionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppVersionService implements CheckAppVersionUseCase {

    private final AppVersionRepositoryPort appVersionRepositoryPort;

    @Override
    public LatestAppVersionResponseDto getLatestVersion(AppPlatform platform) {
        return appVersionRepositoryPort.findLatestByPlatform(platform);
    }
}
