package com.example.bookstore.integration;

import com.example.bookstore.dtos.UserDto;
import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.respositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("tests")
class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USERS_CONTROLLER_URI = "/api/v1/users";

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
        assertNotNull(mockMvc);
        assertNotNull(repository);
        assertNotNull(objectMapper);
    }

    /**
     * Given a User,
     * When calling the register endpoint
     * Then, it should return the user created
     */
    @Test
    @WithAnonymousUser
    void givenUser_whenRegistering_thenReturnSavedUser() throws Exception {
        // given
        UserDto dto = UserDto.builder()
                .username("user")
                .password("password")
                .build();

        String jsonContent = objectMapper.writeValueAsString(dto);

        // when
        ResultActions result = mockMvc.perform(post(USERS_CONTROLLER_URI + "/register")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", equalTo(dto.getUsername())))
                .andExpect(jsonPath("$.password", nullValue()))
        ;
    }

    /**
     * Given a User with username already created,
     * When calling the register endpoint
     * Then, it should return BadRequest
     */
    @Test
    @WithAnonymousUser
    void givenAlreadyCreatedUser_whenRegistering_thenThrowBadRequest() throws Exception {
        // given
        repository.save(UserEntity.builder()
                .username("user")
                .password("password")
                .role("ROLE_USER")
                .build());

        UserDto dto = UserDto.builder()
                .username("user")
                .password("password")
                .build();

        String jsonContent = objectMapper.writeValueAsString(dto);

        // when
        ResultActions result = mockMvc.perform(post(USERS_CONTROLLER_URI + "/register")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isBadRequest())
        ;
    }

    /**
     * Given an invalid user,
     * When calling the register endpoint
     * Then, it should return bad request with a list of missing fields
     */
    @Test
    @WithAnonymousUser
    void givenInvalidUser_whenRegistering_thenThrowBadRequest() throws Exception {
        // given
        UserDto dto = UserDto.builder()
                .username("user")
                // missing required field
                .build();

        String jsonContent = objectMapper.writeValueAsString(dto);

        // when
        ResultActions result = mockMvc.perform(post(USERS_CONTROLLER_URI + "/register")
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.password", notNullValue()))
        ;
    }

}
