package com.example.bookstore.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("tests")
class SecurityPingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String PING_CONTROLLER_URI = "/api/v1/ping";


    /**
     * Given the test objects to support the tests,
     * When starting to test
     * Then, it should assert that all of them are not null
     */
    @Test
    void givenTestObjects_whenTesting_thenAssertNotNull() {
        assertNotNull(mockMvc);
    }

    /*
     * Public endpoint
     */
    /**
     * Given a user with USER role,
     * When calling a public endpoint
     * Then, it should get OK status and get the returned data
     */
    @Test
    @WithMockUser(username = "username", roles = {"USER"})
    void givenUser_whenCallingPingPublicEndpoint_returnACK() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(PING_CONTROLLER_URI));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo("ACK")));
    }

    /**
     * Given a user with ADMIN role,
     * When calling a public endpoint
     * Then, it should get OK status and get the returned data
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAdmin_whenCallingPingPublicEndpoint_returnACK() throws Exception {
        /// when
        ResultActions result = mockMvc.perform(get(PING_CONTROLLER_URI));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo("ACK")));
    }

    /**
     * Given an anonymous user,
     * When calling a public endpoint
     * Then, it should get OK status and get the returned data
     */
    @Test
    @WithAnonymousUser
    void givenAnonymous_whenCallingPingPublicEndpoint_returnACK() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(PING_CONTROLLER_URI));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo("ACK")));
    }

    /*
     * User endpoint
     */
    /**
     * Given a user with USER role,
     * When calling an authenticated endpoint only for users with the USER role
     * Then, it should get OK status and get the returned data
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenUser_whenCallingPingUserAuthenticatedEndpoint_returnACK() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(PING_CONTROLLER_URI + "/user"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo("ACK")));
    }

    /**
     * Given a user with ADMIN role,
     * When calling an authenticated endpoint only for users with the USER role
     * Then, it should get 403 (Forbidden) error
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAdmin_whenCallingPingUserAuthenticatedEndpoint_returnForbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(PING_CONTROLLER_URI + "/user"));

        // then
        result.andExpect(status().isForbidden());
    }

    /**
     * Given an anonymous user,
     * When calling an authenticated endpoint only for users with the USER role
     * Then, it should get 401 (Unauthorized) error
     */
    @Test
    @WithAnonymousUser
    void givenAnonymous_whenCallingPingUserAuthenticatedEndpoint_returnUnauthorized() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(PING_CONTROLLER_URI + "/user"));

        // then
        result.andExpect(status().isUnauthorized());
    }

    /*
     * Admin endpoint
     */
    /**
     * Given a user with ADMIN role,
     * When calling an authenticated endpoint only for users with the ADMIN role
     * Then, it should get 403 (Forbidden) error
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenUser_whenCallingPingAdminAuthenticatedEndpoint_returnForbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(PING_CONTROLLER_URI + "/admin"));

        // then
        result.andExpect(status().isForbidden());
    }

    /**
     * Given a user with ADMIN role,
     * When calling an authenticated endpoint only for users with the ADMIN role
     * Then, it should get OK status and get the returned data
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAdmin_whenCallingPingAdminAuthenticatedEndpoint_returnACK() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(PING_CONTROLLER_URI + "/admin"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo("ACK")));
    }

    /**
     * Given an anonymous user,
     * When calling an authenticated endpoint only for users with the ADMIN role
     * Then, it should get 401 (Unauthorized) error
     */
    @Test
    @WithAnonymousUser
    void givenAnonymous_whenCallingPingAdminAuthenticatedEndpoint_returnUnauthorized() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(PING_CONTROLLER_URI + "/admin"));

        // then
        result.andExpect(status().isUnauthorized());
    }

}
