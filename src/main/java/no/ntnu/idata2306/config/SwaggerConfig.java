package no.ntnu.idata2306.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Swagger documentation.
 * This class defines the Swagger configuration for the application.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates a GroupedOpenApi bean for admin API documentation.
     * This bean configures Swagger to document all endpoints in the application.
     *
     * @return GroupedOpenApi instance configured for admin API documentation.
     */
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/**")
                .build();
    }
}
