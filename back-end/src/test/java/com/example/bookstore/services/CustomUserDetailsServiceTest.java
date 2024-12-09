package com.example.bookstore.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("tests")
class CustomUserDetailsServiceTest {

    /**
     * Given existent user,
     * When calling the loadUserByUsername
     * Then, it should return the UserDetails for that user
     */
    @Test
    @Disabled
    void givenExistentUser_whenCallingLoadUserByUsername_thenReturnUserDetails() {
        // given

        // when

        // then
    }

    /**
     * Given a user that does not exist,
     * When calling the loadUserByUsername
     * Then, it should throw UsernameNotFoundException
     */
    @Test
    @Disabled
    void givenInexistentUser_whenCallingLoadUserByUsername_thenThrowUsernameNotFoundException() {
        // given

        // when

        // then
    }
}
