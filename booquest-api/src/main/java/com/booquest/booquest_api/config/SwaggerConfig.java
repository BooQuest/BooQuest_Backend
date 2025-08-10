package com.booquest.booquest_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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
}
