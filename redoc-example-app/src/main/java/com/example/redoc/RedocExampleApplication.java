package com.example.redoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Example Spring Boot application demonstrating the Redoc starter.
 *
 * <p>Start the application and visit:
 * <ul>
 *   <li>{@code http://localhost:8080/redoc} — Redoc API documentation</li>
 *   <li>{@code http://localhost:8080/v3/api-docs} — OpenAPI JSON spec</li>
 * </ul>
 */
@SpringBootApplication
public class RedocExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedocExampleApplication.class, args);
    }
}
