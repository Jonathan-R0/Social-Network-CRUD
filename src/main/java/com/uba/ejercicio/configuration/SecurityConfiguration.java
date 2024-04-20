package com.uba.ejercicio.configuration;

import com.uba.ejercicio.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserService userService;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/swagger",
            "/api-docs/**",
            "/api/v1/user/password/forgot",
            "/api/v1/user/*/password/recover/**",
            "/api/v1/token",
            "/login",
    };

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                    authorizeRequests -> {
                        authorizeRequests.requestMatchers(AUTH_WHITELIST).permitAll();
                        authorizeRequests.anyRequest().authenticated();
                    }
            )
            .cors()
            .and()
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder)
            throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder)
                .and().build();
    }

}
