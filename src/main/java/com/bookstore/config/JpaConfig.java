package com.bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA configuration to enable auditing.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
