package com.team1.f1_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for the MotoRYX API.
 * Currently configured with permissive settings for early development:
 * all endpoints are publicly accessible, and CSRF, form login,
 * and HTTP Basic authentication are disabled.
 *
 * This configuration should be updated before production to enforce
 * OAuth2 authentication and role-based access control.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the HTTP security filter chain.
     * Disables CSRF protection, form login, and HTTP Basic to allow
     * unrestricted API access during early development.
     *
     * @param http the HttpSecurity object to configure
     * @return the built SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}