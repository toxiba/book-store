package com.example.bookstore.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disabling CSRF, for the simplicity of the kata
                .cors(AbstractHttpConfigurer::disable) // Disabling CORS, for the simplicity of the kata
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/ping").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/books/**").permitAll() // permitting all only the GET endpoints for books
                        .requestMatchers("/api/v1/users/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // make swagger endpoints public, however ...
                        .requestMatchers("/actuator/**").hasRole("ADMIN") // ...actuator endpoints only for ADMINs
                        .anyRequest().authenticated() // Require authentication for all requests
                )
                .httpBasic(Customizer.withDefaults()) // Enabling HTTP Basic Authentication, for the simplicity of the kata
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}