package com.github.maxiamikel.attendancemanagementapi.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Attendance Management API")
                                                .version("1.0")
                                                .description("API para gerenciar turnos de atendimiento")
                                                .contact(new Contact()
                                                                .name("Amikel Maxi")
                                                                .email("maxloversist@gmail.com")))
                                .servers(List.of(
                                                new Server().url("http://localhost:8080").description("Local")))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme()
                                                                                .name("Authorization")
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }
}
