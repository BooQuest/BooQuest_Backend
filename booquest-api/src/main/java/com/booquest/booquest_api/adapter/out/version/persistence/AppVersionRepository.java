package com.booquest.booquest_api.adapter.out.version.persistence;

import com.booquest.booquest_api.adapter.in.version.web.dto.LatestAppVersionResponseDto;
import com.booquest.booquest_api.domain.version.AppPlatform;
import com.booquest.booquest_api.domain.version.AppVersion;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

    @Query("SELECT new com.booquest.booquest_api.adapter.in.version.web.dto.LatestAppVersionResponseDto"
            + "(a.version, a.buildNumber, a.description, a.isForceUpdate) "
            + "FROM AppVersion a "
            + "WHERE a.platform = :platform "
            + "ORDER BY a.releasedAt DESC, a.id DESC")
    List<LatestAppVersionResponseDto> findLatestByPlatform(@Param("platform") AppPlatform platform, Pageable pageable);
}
