package com.booquest.booquest_api.adapter.out.version.persistence;

import com.booquest.booquest_api.adapter.in.version.web.dto.LatestAppVersionResponseDto;
import com.booquest.booquest_api.application.port.out.version.AppVersionRepositoryPort;
import com.booquest.booquest_api.domain.version.AppPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppVersionRepositoryAdapter implements AppVersionRepositoryPort {

    private final AppVersionRepository appVersionRepository;
    @Override
    public LatestAppVersionResponseDto findLatestByPlatform(AppPlatform platform) {
        return appVersionRepository.findLatestByPlatform(platform);
    }
}
