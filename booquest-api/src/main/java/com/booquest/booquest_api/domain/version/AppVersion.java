package com.booquest.booquest_api.domain.version;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "app_versions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_app_versions_platform_version",
                        columnNames = {"platform", "version"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppPlatform platform;   // ANDROID, IOS

    @Column(nullable = false, length = 50)
    private String version;         // 예: "1.2.3"

    @Column(name = "build_number", nullable = false)
    private int buildNumber;    // 예: 6

    @Column(name = "is_force_update", nullable = false)
    private boolean isForceUpdate = false; // 강제 업데이트 여부

    @Column(nullable = false)
    private String description;

    @Column(name = "released_at", nullable = false)
    private OffsetDateTime releasedAt = OffsetDateTime.now(); // 배포 시각
}
