package com.bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA configuration to enable auditing.
 * Note: JPA Auditing is temporarily disabled to avoid circular dependency with
 * Flyway.
 * TODO: Re-enable with proper bean initialization order configuration.
 */
@Configuration
// @EnableJpaAuditing
public class JpaConfig {
}
