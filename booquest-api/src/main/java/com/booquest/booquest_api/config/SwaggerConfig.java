package com.booquest.booquest_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "Boo Quest API",
                description = "부퀘스트 API 문서",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {
        @Value("${app.openapi.server-url:/}")
        private String serverUrl;

        @Bean
        public OpenAPI openAPI() {
                SecurityScheme bearerAuth = new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER);

                SecurityScheme refreshAuth = new SecurityScheme()
                        .name("X-Refresh-Token")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER);

                SecurityRequirement globalRequirement = new SecurityRequirement().addList("bearerAuth");

                Server server = new Server().url(serverUrl).description("OpenAPI base URL");

                return new OpenAPI()
                        .servers(List.of(server))
                        .components(new Components()
                                .addSecuritySchemes("bearerAuth", bearerAuth)
                                .addSecuritySchemes("refreshAuth", refreshAuth))
                        .addSecurityItem(globalRequirement);
        }
}
