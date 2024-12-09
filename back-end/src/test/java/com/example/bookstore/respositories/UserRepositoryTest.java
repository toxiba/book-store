package com.example.bookstore.respositories;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("tests")
public class UserRepositoryTest {

    /**
     * Given a UserEntity,
     * When calling the Save method
     * Then, it should save the entity and return the saved entity
     */
    @Test
    @Disabled
    void givenUser_whenSaving_thenReturnSavedUser() {
        // given

        // when

        // then
    }

    /**
     * Given a valid username,
     * When calling the findById method
     * Then, it should find and return an optional filled with the correct user.
     */
    @Test
    @Disabled
    void givenValidUsername_whenCallingFindById_thenReturnUser() {
        // given

        // when

        // then
    }

    /**
     * Given an invalid username,
     * When calling the findById method
     * Then, it should return an empty optional
     */
    @Test
    @Disabled
    void givenInvalidUsername_whenCallingFindById_thenReturnEmptyOptional() {
        // given

        // when

        // then
    }
}
