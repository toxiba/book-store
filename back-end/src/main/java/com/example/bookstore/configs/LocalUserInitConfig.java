package com.example.bookstore.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@Profile({"local"})
public class LocalUserInitConfig {

    @Value("${users.user.username}")
    private String userUsername;

    @Value("${users.user.password}")
    private String userPassword;

    @Value("${users.user.role}")
    private String userRole;

    @Value("${users.admin.username}")
    private String adminUsername;

    @Value("${users.admin.password}")
    private String adminPassword;

    @Value("${users.admin.role}")
    private String adminRole;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public LocalUserInitConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.builder()
                .username(userUsername)
                .password(passwordEncoder.encode(userPassword))
                .roles(userRole)
                .build();

        var admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles(adminRole)
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

}
