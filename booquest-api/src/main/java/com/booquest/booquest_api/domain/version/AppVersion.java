package com.booquest.booquest_api.domain.version;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_versions", uniqueConstraints = {
        @UniqueConstraint(name = "uq_app_versions_platform", columnNames = "platform")
})
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

    @Column(name = "latest_version", nullable = false, length = 50)
    private String latestVersion;

    @Column(name = "latest_build_number", nullable = false)
    private Integer latestBuildNumber;
}
