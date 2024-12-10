package com.example.bookstore.configs;

import com.example.bookstore.mappers.*;
import com.example.bookstore.services.IBookService;
import com.example.bookstore.services.ICartService;
import com.example.bookstore.services.IUserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
@EnableMethodSecurity
public class TestConfig {

    @Bean
    public IBookService bookService() {
        return Mockito.mock(IBookService.class);
    }

    @Bean
    public IUserService userService() {
        return Mockito.mock(IUserService.class);
    }

    @Bean
    public ICartService cartService() {
        return Mockito.mock(ICartService.class);
    }


    @Bean
    public BookMapper bookMapper() {
        return new BookMapperImpl();
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }

    @Bean
    public CartMapper cartMapper() {
        return new CartMapperImpl();
    }

    @Bean
    public CartCheckoutMapper cartCheckoutMapper() {
        return new CartCheckoutMapperImpl();
    }

    @Bean
    public CartItemMapper cartItemMapper() {
        return new CartItemMapperImpl();
    }

    @Bean
    public CartItemCheckoutMapper cartItemCheckoutMapper() {
        return new CartItemCheckoutMapperImpl();
    }

    /**
     * A simple version of the security config only for tests
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/ping").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/books/**").permitAll()
                        .requestMatchers("/api/v1/users/register").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

}
