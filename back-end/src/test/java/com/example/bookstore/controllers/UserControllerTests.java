package com.example.bookstore.controllers;

import com.example.bookstore.configs.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
@Import({TestConfig.class})
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BOOKS_CONTROLLER_URI = "/api/v1/users";

    /**
     * Given the test objects to support the tests,
     * When starting to test
     * Then, it should assert that all of them are not null
     */
    @Test
    void givenTestObjects_whenTesting_thenAssertNotNull() {
        assertNotNull(mockMvc);
        assertNotNull(objectMapper);
    }

    /**
     * Given a User,
     * When calling the register endpoint
     * Then, it should return the user created
     */
    @Test
    @Disabled
    void givenUser_whenRegistering_thenReturnSavedUser() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given a User with username already created,
     * When calling the register endpoint
     * Then, it should return BadRequest
     */
    @Test
    @Disabled
    void givenAlreadyCreatedUser_whenRegistering_thenThrowBadRequest() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given an invalid user,
     * When calling the register endpoint
     * Then, it should return bad request with a list of missing fields
     */
    @Test
    @Disabled
    void givenInvalidUser_whenRegistering_thenThrowBadRequest() throws Exception {
        // given

        // when

        // then
    }

}
