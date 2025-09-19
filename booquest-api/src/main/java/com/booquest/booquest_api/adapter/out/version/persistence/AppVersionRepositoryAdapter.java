package com.booquest.booquest_api.adapter.out.version.persistence;

import com.booquest.booquest_api.adapter.in.version.web.dto.LatestAppVersionResponseDto;
import com.booquest.booquest_api.application.port.out.version.AppVersionRepositoryPort;
import com.booquest.booquest_api.domain.version.AppPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppVersionRepositoryAdapter implements AppVersionRepositoryPort {

    private final AppVersionRepository appVersionRepository;
    @Override
    public LatestAppVersionResponseDto findLatestByPlatform(AppPlatform platform) {
        Pageable limitOne = PageRequest.of(0, 1);

        return appVersionRepository.findLatestByPlatform(platform, limitOne)
                    .stream()
                    .findFirst()
                    .orElse(null);
    }
}
