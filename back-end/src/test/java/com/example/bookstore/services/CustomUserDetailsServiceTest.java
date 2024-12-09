package com.example.bookstore.services;

import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.respositories.UserRepository;
import com.example.bookstore.services.impls.CustomUserDetailsService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("tests")
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService service;

    @Mock
    private UserRepository repository;

    /**
     * Given the test objects to support the tests,
     * When starting to test
     * Then, it should assert that all of them are not null
     */
    @Test
    void givenTestObjects_whenTesting_thenAssertNotNull() {
        assertNotNull(service);
        assertNotNull(repository);
    }

    /**
     * Given existent user,
     * When calling the loadUserByUsername
     * Then, it should return the UserDetails for that user
     */
    @Test
    void givenExistentUser_whenCallingLoadUserByUsername_thenReturnUserDetails() {
        // given
        String username = "user";

        UserEntity entity = UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build();

        when(repository.findById(anyString())).thenReturn(Optional.of(entity));

        // when
        UserDetails actual = service.loadUserByUsername(username);

        // then
        assertNotNull(actual);
        assertEquals(entity.getUsername(), actual.getUsername());
        assertEquals(entity.getPassword(), actual.getPassword());
        assertNotNull(actual.getAuthorities());
        assertTrue(actual.getAuthorities().stream().anyMatch(a -> entity.getRole().equals(a.getAuthority())));
    }

    /**
     * Given a user that does not exist,
     * When calling the loadUserByUsername
     * Then, it should throw UsernameNotFoundException
     */
    @Test
    void givenInexistentUser_whenCallingLoadUserByUsername_thenThrowUsernameNotFoundException() {
        // given
        String username = "user";

        when(repository.findById(anyString())).thenReturn(Optional.empty());

        // when/then
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(username));
    }
}
