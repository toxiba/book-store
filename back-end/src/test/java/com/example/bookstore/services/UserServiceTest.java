package com.example.bookstore.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("tests")
public class UserServiceTest {

    /**
     * Given a UserEntity,
     * When calling the create method
     * Then, it should save the entity and return the created entity
     */
    @Test
    @Disabled
    void givenUser_whenCreating_thenReturnSavedUser() {
        // given

        // when

        // then
    }
}
