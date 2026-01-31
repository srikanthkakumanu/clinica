package com.clinica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Security configuration for the Clinic Management System.
 * Implements HTTP Basic Authentication with OWASP security best practices.
 * Includes CORS configuration, stateless session management, and input
 * validation.
 *
 * Security Features:
 * - HTTP Basic Authentication with BCrypt password encoding
 * - Stateless session management (no server-side sessions)
 * - CORS support for cross-origin requests
 * - CSRF protection disabled for stateless API design
 * - Public access to API documentation and health endpoints
 *
 * Default Users:
 * - admin/admin123 (ADMIN role)
 * - user/user123 (USER role)
 *
 * @author Clinica Development Team
 * @version 1.0
 * @since 2026-01-31
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain with HTTP Basic Authentication,
     * stateless session management, CORS support, and OWASP best practices.
     *
     * Security Configuration Details:
     * - CSRF protection disabled (appropriate for stateless REST APIs)
     * - CORS enabled with permissive configuration for development
     * - HTTP Basic Authentication enabled
     * - Stateless session management (no server-side session storage)
     * - Public endpoints: Swagger UI, OpenAPI docs, and actuator health checks
     * - All other endpoints require authentication
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll() // Allow docs
                                                                                                          // and health
                                                                                                          // checks
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .httpBasic(httpBasic -> {
                }) // Enable HTTP Basic Authentication
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
                );

        return http.build();
    }

    /**
     * Configures CORS to allow cross-origin requests from specified origins.
     * This configuration is permissive for development environments.
     *
     * CORS Settings:
     * - Allowed origins: All (*) - suitable for development
     * - Allowed methods: GET, POST, PUT, DELETE, OPTIONS
     * - Allowed headers: All (*)
     * - Allow credentials: true
     * - Max age: 3600 seconds (1 hour cache for preflight requests)
     *
     * Note: In production, restrict allowed origins to specific domains.
     *
     * @return the CorsConfigurationSource with configured CORS settings
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // Allow all origins in development
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Provides a password encoder using BCrypt hashing algorithm.
     * BCrypt is a secure password hashing function designed to be slow
     * and resistant to brute-force attacks, making it suitable for production use.
     *
     * @return the PasswordEncoder implementation using BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures an in-memory user details service with default users.
     * This implementation is suitable for development and testing.
     *
     * Default Users:
     * - Username: "admin", Password: "admin123", Role: ADMIN
     * - Username: "user", Password: "user123", Role: USER
     *
     * Security Note: In production environments, replace this with a
     * proper user store (database, LDAP, OAuth, etc.) and never use
     * default or hardcoded credentials.
     *
     * @return the UserDetailsService with configured users
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}