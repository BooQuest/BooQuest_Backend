package com.booquest.booquest_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Boo Quest API",
                description = "부퀘스트 API 문서",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {
        @Bean
        public OpenAPI openAPI() {
                // Bearer JWT (Authorization 헤더)
                SecurityScheme bearerAuth = new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER);

                // (선택) RefreshToken 헤더용 스키마 - 헤더로 refreshToken을 받는 방식일 경우 사용, 쿠키(HttpOnly) 기반일 경우 미사용
                SecurityScheme refreshAuth = new SecurityScheme()
                        .name("X-Refresh-Token")
                        .type(SecurityScheme.Type.APIKEY)     // 헤더 단순 키 전송
                        .in(SecurityScheme.In.HEADER);

                // 기본적으로 모든 API에 Bearer 인증 요구
                SecurityRequirement globalRequirement = new SecurityRequirement().addList("bearerAuth");

                return new OpenAPI()
                        .components(new Components()
                                .addSecuritySchemes("bearerAuth", bearerAuth)
                                .addSecuritySchemes("refreshAuth", refreshAuth))
                        .addSecurityItem(globalRequirement);
        }
}
