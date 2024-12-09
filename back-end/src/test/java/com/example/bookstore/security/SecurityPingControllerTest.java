package com.example.bookstore.security;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    @Disabled
    void givenUser_whenCallingPingPublicEndpoint_returnACK() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given a user with ADMIN role,
     * When calling a public endpoint
     * Then, it should get OK status and get the returned data
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Disabled
    void givenAdmin_whenCallingPingPublicEndpoint_returnACK() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given an anonymous user,
     * When calling a public endpoint
     * Then, it should get OK status and get the returned data
     */
    @Test
    @WithAnonymousUser
    @Disabled
    void givenAnonymous_whenCallingPingPublicEndpoint_returnACK() throws Exception {
        // given

        // when

        // then
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
    @Disabled
    void givenUser_whenCallingPingUserAuthenticatedEndpoint_returnACK() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given a user with ADMIN role,
     * When calling an authenticated endpoint only for users with the USER role
     * Then, it should get 403 (Forbidden) error
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Disabled
    void givenAdmin_whenCallingPingUserAuthenticatedEndpoint_returnForbidden() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given an anonymous user,
     * When calling an authenticated endpoint only for users with the USER role
     * Then, it should get 401 (Unauthorized) error
     */
    @Test
    @WithAnonymousUser
    @Disabled
    void givenAnonymous_whenCallingPingUserAuthenticatedEndpoint_returnUnauthorized() throws Exception {
        // given

        // when

        // then
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
    @Disabled
    void givenUser_whenCallingPingAdminAuthenticatedEndpoint_returnForbidden() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given a user with ADMIN role,
     * When calling an authenticated endpoint only for users with the ADMIN role
     * Then, it should get OK status and get the returned data
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Disabled
    void givenAdmin_whenCallingPingAdminAuthenticatedEndpoint_returnACK() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given an anonymous user,
     * When calling an authenticated endpoint only for users with the ADMIN role
     * Then, it should get 401 (Unauthorized) error
     */
    @Test
    @WithAnonymousUser
    @Disabled
    void givenAnonymous_whenCallingPingAdminAuthenticatedEndpoint_returnUnauthorized() throws Exception {
        // given

        // when

        // then
    }

}
