package com.team1.f1_api.config;

import com.team1.f1_api.service.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security configuration for the MotoRYX API.
 * Enables Google OAuth2 login. After successful authentication,
 * the user is redirected to the frontend. API data endpoints
 * (tracks, vehicles, laps) are publicly accessible.
 * Auth-related endpoints (/auth/**, /me) require login.
 */
@Configuration
public class SecurityConfig {

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    private final AppUserService appUserService;

    public SecurityConfig(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * Configures the HTTP security filter chain.
     * - CORS enabled for frontend origin
     * - CSRF disabled for API usage
     * - Public data endpoints are open
     * - /auth/** and /me require authentication
     * - Google OAuth2 login enabled with redirect to frontend on success
     *
     * @param http the HttpSecurity object to configure
     * @return the built SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .userDetailsService(appUserService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/tracks/**", "/vehicles/**", "/laps/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/tracks/**", "/vehicles/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tracks/**", "/vehicles/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tracks/**", "/vehicles/**").hasAuthority("ADMIN")
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/auth/me").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(u -> u.userService(appUserService))
                        .successHandler(oAuth2SuccessHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl(frontendUrl)
                );

        return http.build();
    }

    /**
     * Redirects to the frontend after successful Google OAuth2 login.
     */
    private AuthenticationSuccessHandler oAuth2SuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) -> {
            response.sendRedirect(frontendUrl);
        };
    }

    /**
     * Configures CORS to allow the frontend origin.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontendUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
