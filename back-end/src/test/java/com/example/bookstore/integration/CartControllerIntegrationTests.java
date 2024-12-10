package com.example.bookstore.integration;

import com.example.bookstore.entities.BookEntity;
import com.example.bookstore.entities.CartEntity;
import com.example.bookstore.entities.CartItemEntity;
import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.respositories.BookRepository;
import com.example.bookstore.respositories.CartRepository;
import com.example.bookstore.respositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("tests")
class CartControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CART_CONTROLLER_URI = "/api/v1/cart";

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
        userRepository.deleteAll();
        bookRepository.deleteAll();
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
        assertNotNull(userRepository);
        assertNotNull(bookRepository);
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
        userRepository.save(UserEntity.builder()
            .username("user")
            .password("password")
            .role("ROLE_USER")
            .build());

        Integer bookId = 1;
        Integer quantity = 2;

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
        String username = "user";
        Integer quantity = 2;

        userRepository.save(UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build());

        BookEntity bookEntity = bookRepository.save(BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build());

        Integer bookId = bookEntity.getId();

        // when
        ResultActions result = mockMvc.perform(post(CART_CONTROLLER_URI + "/item/" + bookId + "/quantity/" + quantity));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalToObject(username)))
                .andExpect(jsonPath("$.items[0].quantity", equalToObject(quantity)))
                .andExpect(jsonPath("$.items[0].book.title", equalToObject(bookEntity.getTitle())))
        ;
    }

    /**
     * Given created Cart with Item, a username, a book id to increase its quantity,
     * When adding item into the cart
     * Then, it should update the cart for the user and increase the book quantity
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenCreatedCart_whenAddingItemAlreadyThere_thenReturnUpdateCartIncreasingItemQuantity() throws Exception {
        // given
        String username = "user";
        Integer quantity = 2;

        Integer quantityAlreadyAdded = 3;

        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build());

        BookEntity bookEntity = bookRepository.save(BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build());

        Integer bookId = bookEntity.getId();

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .book(bookEntity)
                .quantity(quantityAlreadyAdded)
                .build();

        CartEntity cartEntity = repository.save(CartEntity.builder()
                .user(userEntity)
                .build());

        cartEntity.addItem(cartItemEntity);
        repository.save(cartEntity);

        // when
        ResultActions result = mockMvc.perform(post(CART_CONTROLLER_URI + "/item/" + bookId + "/quantity/" + quantity));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalToObject(username)))
                .andExpect(jsonPath("$.items[0].quantity", equalToObject(quantityAlreadyAdded + quantity)))
                .andExpect(jsonPath("$.items[0].book.title", equalToObject(bookEntity.getTitle())))
        ;
    }

    /**
     * Given a cart with an item and a book to remove from the cart,
     * When deleting a book from a cart
     * Then, it should remove the book from the cart
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenCartAndBookToRemove_whenDeletingBookFromCart_thenReturnCartWithoutBook() throws Exception {
        String username = "user";

        Integer quantityAlreadyAdded = 3;

        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build());

        BookEntity bookEntity = bookRepository.save(BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build());

        Integer bookId = bookEntity.getId();

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .book(bookEntity)
                .quantity(quantityAlreadyAdded)
                .build();

        CartEntity cartEntity = repository.save(CartEntity.builder()
                .user(userEntity)
                .build());

        cartEntity.addItem(cartItemEntity);
        repository.save(cartEntity);

        // when
        ResultActions result = mockMvc.perform(delete(CART_CONTROLLER_URI + "/item/" + bookId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalToObject(username)))
                .andExpect(jsonPath("$.items", hasSize(0)))
        ;
    }

    /**
     * Given a cart with an item and a book to remove from the cart that is not in the cart,
     * When deleting a book from a cart
     * Then, it shouldn't do anything, just move on and return the cart as it was
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenCartAndInvaliBookToRemove_whenDeletingBookFromCart_thenReturnCart() throws Exception {
        String username = "user";

        Integer quantityAlreadyAdded = 3;

        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build());

        BookEntity bookEntity = bookRepository.save(BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build());

        BookEntity bookEntityNotPresent = bookRepository.save(BookEntity.builder()
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(123))
                .build());

        Integer bookId = bookEntityNotPresent.getId();

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .book(bookEntity)
                .quantity(quantityAlreadyAdded)
                .build();

        CartEntity cartEntity = repository.save(CartEntity.builder()
                .user(userEntity)
                .build());

        cartEntity.addItem(cartItemEntity);
        repository.save(cartEntity);

        // when
        ResultActions result = mockMvc.perform(delete(CART_CONTROLLER_URI + "/item/" + bookId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalToObject(username)))
                .andExpect(jsonPath("$.items", hasSize(1)))
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
        String username = "user";

        Integer quantityAlreadyAdded = 3;

        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build());

        BookEntity bookEntity = bookRepository.save(BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build());

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .book(bookEntity)
                .quantity(quantityAlreadyAdded)
                .build();

        CartEntity cartEntity = repository.save(CartEntity.builder()
                .user(userEntity)
                .build());

        cartEntity.addItem(cartItemEntity);
        repository.save(cartEntity);

        // when
        ResultActions result = mockMvc.perform(get(CART_CONTROLLER_URI));


        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalToObject(username)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].book.title", equalToObject(bookEntity.getTitle())))
        ;
    }

    /**
     * Given a not yet created cart for a user
     * When getting the cart
     * Then, return a new cart without items
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenNotYetCreatedCart_whenGettingCart_thenReturnNewCart() throws Exception {
        // given
        String username = "user";

        userRepository.save(UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build());

        // when
        ResultActions result = mockMvc.perform(get(CART_CONTROLLER_URI));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalToObject(username)))
                .andExpect(jsonPath("$.items", hasSize(0)))
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
        String username = "user";

        Integer quantity = 3;
        Integer price = 123;
        Double subTotal = 369.0; // quantity * price

        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build());

        BookEntity bookEntity = bookRepository.save(BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(price))
                .build());

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .book(bookEntity)
                .quantity(quantity)
                .build();

        CartEntity cartEntity = repository.save(CartEntity.builder()
                .user(userEntity)
                .build());

        cartEntity.addItem(cartItemEntity);
        repository.save(cartEntity);

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
