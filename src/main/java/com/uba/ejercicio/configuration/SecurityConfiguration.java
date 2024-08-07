package com.uba.ejercicio.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/swagger",
            "/api-docs/**",
            "/auth/refresh-token",
            "/auth/login",
            "/auth/logout",
            "/user/password/forgot",
            "/user/password/*/recover/*",
            "/auth/user/*/validation/**"
    };

    @Autowired
    private AuthenticationFilter authenticationFilter;

    private static final String ADMIN_ROLE = "ADMIN";

    private static final String USER_ROLE = "USER";

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                    authorizeRequests -> {
                        authorizeRequests.requestMatchers(AUTH_WHITELIST).permitAll();
                        authorizeRequests.requestMatchers(HttpMethod.POST, "/api/v1/user")
                                .permitAll();
                        authorizeRequests.requestMatchers(HttpMethod.GET, "/api/v1/user/gender")
                                .hasAnyAuthority(ADMIN_ROLE, USER_ROLE);
                        authorizeRequests.requestMatchers(HttpMethod.DELETE, "/api/v1/user/gender", "api/v1/user")
                                .hasAuthority(ADMIN_ROLE);
                        authorizeRequests.requestMatchers(HttpMethod.DELETE, "api/v1/user/self")
                                .hasAnyAuthority(ADMIN_ROLE, USER_ROLE);
                        authorizeRequests.requestMatchers(HttpMethod.POST, "/api/v1/user/gender")
                                .hasAuthority(ADMIN_ROLE);
                        authorizeRequests.requestMatchers("/api/v1/user/follower", "/api/v1/user/following", "/api/v1/user/profile")
                                .hasAnyAuthority(ADMIN_ROLE, USER_ROLE);
                        authorizeRequests.anyRequest().authenticated();
                    }
            )
            .cors(httpSecurityCorsConfigurer ->
                    httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource())
            )
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:8080"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
