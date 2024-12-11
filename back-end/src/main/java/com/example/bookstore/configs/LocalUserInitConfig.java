package com.example.bookstore.configs;

import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.respositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

@Slf4j
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

    private UserRepository repository;

    @Autowired
    public LocalUserInitConfig(UserRepository repository) {
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        log.info("Inserting predefined user locally with username = {}, password = {} and role = {}", userUsername, userPassword, userRole);
        repository.save(UserEntity.builder()
                .username(userUsername)
                .password("{noop}" + userPassword)
                .role(userRole)
                .build());

        log.info("Inserting predefined user with username = {}, password = {} and role = {}", adminUsername, adminPassword, adminRole);
        repository.save(UserEntity.builder()
                .username(adminUsername)
                .password("{noop}" + adminPassword)
                .role(adminRole)
                .build());
    }

}
