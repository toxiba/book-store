package com.example.bookstore.services;

import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.exceptions.AlreadyExistsException;
import com.example.bookstore.respositories.UserRepository;
import com.example.bookstore.services.impls.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("tests")
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl service;

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
     * Given a UserEntity,
     * When calling the create method
     * Then, it should save the entity and return the created entity
     */
    @Test
    void givenUser_whenCreating_thenReturnSavedUser() {
        // given
        UserEntity entity = UserEntity.builder()
                .username("user")
                .password("password")
                .role("ROLE_USER")
                .build();

        doReturn(Optional.empty()).when(repository).findById(anyString());
        doAnswer(invocation -> invocation.getArgument(0)).when(repository).save(any(UserEntity.class));

        // when
        UserEntity actual = service.createUser(entity);

        // then
        assertNotNull(actual);
        assertEquals(entity.getUsername(), actual.getUsername());
        assertEquals(entity.getPassword(), actual.getPassword());
        assertEquals(entity.getRole(), actual.getRole());
    }

    /**
     * Given a UserEntity with an already existing username,
     * When calling the create method
     * Then, it should throw AlreadyExistsException
     */
    @Test
    void givenAlreadyExistingUser_whenCreating_thenThrowAlreadyExistsException() {
        // given
        UserEntity entity = UserEntity.builder()
                .username("user")
                .password("password")
                .role("ROLE_USER")
                .build();

        doReturn(Optional.of(entity)).when(repository).findById(anyString());

        // when/then
        assertThrows(AlreadyExistsException.class, () -> service.createUser(entity));
    }
}
