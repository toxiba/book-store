package com.example.bookstore.respositories;

import com.example.bookstore.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("tests")
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    /**
     * Given the test objects to support the tests,
     * When starting to test
     * Then, it should assert that all of them are not null
     */
    @Test
    void givenTestObjects_whenTesting_thenAssertNotNull() {
        assertNotNull(repository);
    }

    /**
     * Given a UserEntity,
     * When calling the Save method
     * Then, it should save the entity and return the saved entity
     */
    @Test
    void givenUser_whenSaving_thenReturnSavedUser() {
        // given
        UserEntity entity = UserEntity.builder()
                .username("user")
                .password("password")
                .role("ROLE_USER")
                .build();

        // when
        UserEntity actual = repository.save(entity);

        // then
        assertNotNull(actual);
        assertEquals(entity.getUsername(), actual.getUsername());
        assertEquals(entity.getPassword(), actual.getPassword());
        assertEquals(entity.getRole(), actual.getRole());
    }

    /**
     * Given a valid username,
     * When calling the findById method
     * Then, it should find and return an optional filled with the correct user.
     */
    @Test
    void givenValidUsername_whenCallingFindById_thenReturnUser() {
        // given
        String validUsername = "user";

        UserEntity entity = UserEntity.builder()
                .username("user")
                .password("password")
                .role("ROLE_USER")
                .build();

        repository.save(entity);

        // when
        Optional<UserEntity> actual = repository.findById(validUsername);

        // then
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(entity.getUsername(), actual.get().getUsername());
        assertEquals(entity.getPassword(), actual.get().getPassword());
        assertEquals(entity.getRole(), actual.get().getRole());
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
        String invalidUsername = "invalid";

        UserEntity entity = UserEntity.builder()
                .username("user")
                .password("password")
                .role("ROLE_USER")
                .build();

        repository.save(entity);

        // when
        Optional<UserEntity> actual = repository.findById(invalidUsername);

        // then
        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }
}
