package com.example.bookstore.controllers;

import com.example.bookstore.configs.TestConfig;
import com.example.bookstore.dtos.UserDto;
import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.exceptions.AlreadyExistsException;
import com.example.bookstore.mappers.UserMapper;
import com.example.bookstore.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
@Import({TestConfig.class})
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserService service;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USERS_CONTROLLER_URI = "/api/v1/users";

    /**
     * Given the test objects to support the tests,
     * When starting to test
     * Then, it should assert that all of them are not null
     */
    @Test
    void givenTestObjects_whenTesting_thenAssertNotNull() {
        assertNotNull(mockMvc);
        assertNotNull(service);
        assertNotNull(mapper);
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

        doAnswer(invocation -> invocation.getArgument(0))
                .when(service).createUser(ArgumentMatchers.any(UserEntity.class));

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
        UserDto dto = UserDto.builder()
                .username("user")
                .password("password")
                .build();

        doThrow(AlreadyExistsException.class).when(service).createUser(ArgumentMatchers.any(UserEntity.class));

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
