package com.booquest.booquest_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@Profile("!ai_test")   // ai_test일 땐 Auditing 비활성
@EnableJpaAuditing
public class JpaAuditingConfig {
}
