package com.db.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ObjectMapper objectMapper;
    private final OpenAPI openAPI;

    @Value("${swagger.server.url.local}")
    private String localServerUrl;

    @Value("${swagger.server.url.production}")
    private String productionServerUrl;

    private Environment environment;

    @Autowired
    public SwaggerConfig(ObjectMapper objectMapper, ResourceLoader resourceLoader, Environment environment) throws IOException {
        this.objectMapper = objectMapper;
        this.openAPI = loadApiDocs(resourceLoader.getResource("classpath:/api-docs.json").getInputStream());
        this.environment = environment;
    }

    private OpenAPI loadApiDocs(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, OpenAPI.class);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        String[] activeProfiles = environment.getActiveProfiles();
        String serverUrl = "prod".equals(activeProfiles[0]) ? productionServerUrl : localServerUrl;
        openAPI.setServers(Collections.singletonList(new Server().url(serverUrl)));
        return openAPI;
    }
}