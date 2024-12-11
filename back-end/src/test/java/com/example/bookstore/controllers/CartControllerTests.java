package com.example.bookstore.controllers;

import com.example.bookstore.configs.TestConfig;
import com.example.bookstore.entities.BookEntity;
import com.example.bookstore.entities.CartEntity;
import com.example.bookstore.entities.CartItemEntity;
import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.exceptions.NotFoundException;
import com.example.bookstore.mappers.CartCheckoutMapper;
import com.example.bookstore.mappers.CartMapper;
import com.example.bookstore.services.ICartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalToObject;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
@Import({TestConfig.class})
class CartControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICartService service;

    @Autowired
    private CartMapper mapper;

    @Autowired
    private CartCheckoutMapper cartCheckoutMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CART_CONTROLLER_URI = "/api/v1/cart";

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
        assertNotNull(cartCheckoutMapper);
        assertNotNull(objectMapper);
    }

    /**
     * Given an invalid path parameter
     * When adding item into the cart
     * Then, it should return a BadRequest
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenInvalidPathParameter_whenAddingItems_thenReturnBadRequest() throws Exception {
        // given
        Integer bookId = 1;
        Integer quantity = 0; // invalid

        // when
        ResultActions result = mockMvc.perform(post(CART_CONTROLLER_URI + "/item/" + bookId + "/quantity/" + quantity));

        // then
        result.andExpect(status().isBadRequest())
        ;
    }

    /**
     * Given an invalid username
     * When adding item into the cart
     * Then, it should throw a NotFoundException
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenInvalidUsername_whenAddingItems_thenThrowNotFoundException() throws Exception {
        // given
        Integer bookId = 1;
        Integer quantity = 2;

        doThrow(NotFoundException.class).when(service).addItem(anyString(), anyInt(), anyInt());

        // when
        ResultActions result = mockMvc.perform(post(CART_CONTROLLER_URI + "/item/" + bookId + "/quantity/" + quantity));

        // then
        result.andExpect(status().isNotFound())
        ;
    }

    /**
     * Given an invalid bookId
     * When adding item into the cart
     * Then, it should throw a NotFoundException
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenInvalidBookId_whenAddingItems_thenThrowNotFoundException() throws Exception {
        // given
        Integer bookId = 1;
        Integer quantity = 2;

        doThrow(NotFoundException.class).when(service).addItem(anyString(), anyInt(), anyInt());

        // when
        ResultActions result = mockMvc.perform(post(CART_CONTROLLER_URI + "/item/" + bookId + "/quantity/" + quantity));

        // then
        result.andExpect(status().isNotFound())
        ;
    }

    /**
     * Given Not yet created Cart, a username, a book id to be added and a quantity,
     * When adding item into the cart
     * Then, it should create the cart for the user and add the book
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenNotYetCreatedCart_whenAddingItems_thenReturnCreatedCartWithItem() throws Exception {
        // given
        Integer bookId = 1;
        Integer quantity = 2;

        String username = "user";
        Integer quantityAlreadyAdded = 3;

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build();

        BookEntity bookEntity = BookEntity.builder()
                .id(1)
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build();

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .book(bookEntity)
                .quantity(quantityAlreadyAdded)
                .build();

        CartEntity cartEntity = CartEntity.builder()
                .user(userEntity)
                .build();

        cartEntity.addItem(cartItemEntity);

        doReturn(cartEntity).when(service).addItem(anyString(), anyInt(), anyInt());

        // when
        ResultActions result = mockMvc.perform(post(CART_CONTROLLER_URI + "/item/" + bookId + "/quantity/" + quantity));


        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalToObject(username)))
                .andExpect(jsonPath("$.items[0].quantity", equalToObject(quantityAlreadyAdded)))
                .andExpect(jsonPath("$.items[0].book.title", equalToObject(bookEntity.getTitle())))
        ;
    }

    /**
     * Given an already created cart with an item
     * When getting the cart
     * Then, return the cart
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenCart_whenGettingCart_thenReturnCart() throws Exception {
        // given
        Integer quantity = 2;
        String username = "user";

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build();

        BookEntity bookEntity = BookEntity.builder()
                .id(1)
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build();

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .book(bookEntity)
                .quantity(quantity)
                .build();

        CartEntity cartEntity = CartEntity.builder()
                .user(userEntity)
                .build();

        cartEntity.addItem(cartItemEntity);

        doReturn(cartEntity).when(service).getCart(anyString());

        // when
        ResultActions result = mockMvc.perform(get(CART_CONTROLLER_URI));


        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalToObject(username)))
                .andExpect(jsonPath("$.items[0].quantity", equalToObject(quantity)))
                .andExpect(jsonPath("$.items[0].book.title", equalToObject(bookEntity.getTitle())))
        ;
    }

    /**
     * Given an anonymous user
     * When getting the cart
     * Then, return Unauthorized
     */
    @Test
    @WithAnonymousUser
    void givenAnonymous_whenGettingCart_thenReturnUnauthorized() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(CART_CONTROLLER_URI));

        // then
        result.andExpect(status().isUnauthorized())
        ;
    }

    /**
     * Given an admin
     * When getting the cart
     * Then, return Forbidden
     */
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void givenAdmin_whenGettingCart_thenReturnForbidden() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(CART_CONTROLLER_URI));

        // then
        result.andExpect(status().isForbidden())
        ;
    }

    /**
     * Given an already created cart with an item
     * When checking out
     * Then, return the checkout
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenCart_whenCheckingOut_thenReturnCheckout() throws Exception {
        // given
        Integer quantity = 2;
        String username = "user";
        Integer subTotal = 246; // 123 * 2;

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build();

        BookEntity bookEntity = BookEntity.builder()
                .id(1)
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build();

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .book(bookEntity)
                .quantity(quantity)
                .build();

        CartEntity cartEntity = CartEntity.builder()
                .user(userEntity)
                .build();

        cartEntity.addItem(cartItemEntity);

        doReturn(cartEntity).when(service).getCart(anyString());

        // when
        ResultActions result = mockMvc.perform(post(CART_CONTROLLER_URI + "/checkout"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalToObject(username)))
                .andExpect(jsonPath("$.items[0].subtotal", is(subTotal)))
                .andExpect(jsonPath("$.total", is(subTotal)))
        ;
    }

}
