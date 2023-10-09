package com.db.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ObjectMapper objectMapper;
    private final OpenAPI openAPI;

    @Autowired
    public SwaggerConfig(ObjectMapper objectMapper, ResourceLoader resourceLoader) throws IOException {
        this.objectMapper = objectMapper;
        this.openAPI = loadApiDocs(resourceLoader.getResource("classpath:/api-docs.json").getInputStream());
    }

    private OpenAPI loadApiDocs(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, OpenAPI.class);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return openAPI;
    }

}