package com.bookstore.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration.
 */
@Configuration
public class OpenApiConfig {

        @Value("${spring.application.name}")
        private String applicationName;

        @Bean
        public OpenAPI customOpenApi() {
                final String securitySchemeName = "bearerAuth";

                return new OpenAPI()
                                .info(new Info()
                                                .title("Book Store API")
                                                .version("1.0.0")
                                                .description(
                                                                "Professional RESTful API for Book Store management system with JWT authentication")
                                                .contact(new Contact()
                                                                .name("Your Name")
                                                                .email("your.email@example.com")
                                                                .url("https://github.com/yourusername/book-store-service"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("https://opensource.org/licenses/MIT")))
                                .servers(List.of(
                                                new Server().url("http://localhost:8080")
                                                                .description("Development Server"),
                                                new Server().url("https://api.bookstore.com")
                                                                .description("Production Server")))
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                                .components(new Components()
                                                .addSecuritySchemes(securitySchemeName,
                                                                new SecurityScheme()
                                                                                .name(securitySchemeName)
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")
                                                                                .description("Enter JWT token")));
        }
}
