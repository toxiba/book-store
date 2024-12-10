package com.example.bookstore.services;

import com.example.bookstore.entities.BookEntity;
import com.example.bookstore.entities.CartEntity;
import com.example.bookstore.entities.CartItemEntity;
import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.exceptions.NotFoundException;
import com.example.bookstore.respositories.BookRepository;
import com.example.bookstore.respositories.CartRepository;
import com.example.bookstore.respositories.UserRepository;
import com.example.bookstore.services.impls.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("tests")
class CartServiceTest {


    @InjectMocks
    private CartServiceImpl service;

    @Mock
    private CartRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    /**
     * Given the test objects to support the tests,
     * When starting to test
     * Then, it should assert that all of them are not null
     */
    @Test
    void givenTestObjects_whenTesting_thenAssertNotNull() {
        assertNotNull(service);
        assertNotNull(repository);
        assertNotNull(userRepository);
        assertNotNull(bookRepository);
    }

    /**
     * Given an invalid username
     * When adding item into the cart
     * Then, it should throw a NotFoundException
     */
    @Test
    void givenInvalidUsername_whenAddingItems_thenThrowNotFoundException() {
        // given
        String username = "invalid";
        Integer bookId = 1;
        Integer quantity = 2;

        doReturn(Optional.empty()).when(userRepository).findById(anyString());

        // when/then
        assertThrows(NotFoundException.class, () -> service.addItem(username, bookId, quantity));
    }

    /**
     * Given an invalid bookId
     * When adding item into the cart
     * Then, it should throw a NotFoundException
     */
    @Test
    void givenInvalidBookId_whenAddingItems_thenThrowNotFoundException() {
        // given
        String username = "user";
        Integer bookId = 1;
        Integer quantity = 2;

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build();

        CartEntity entity = CartEntity.builder()
                .user(userEntity)
                .build();

        doReturn(Optional.of(userEntity)).when(userRepository).findById(anyString());
        doReturn(Optional.of(entity)).when(repository).findByUser(any(UserEntity.class));
        doReturn(Optional.empty()).when(bookRepository).findById(anyInt());

        // when/then
        assertThrows(NotFoundException.class, () -> service.addItem(username, bookId, quantity));
    }

    /**
     * Given Not yet created Cart, a username, a book id to be added and a quantity,
     * When adding item into the cart
     * Then, it should create the cart for the user and add the book
     */
    @Test
    void givenNotYetCreatedCart_whenAddingItems_thenReturnCreatedCartWithItem() {
        // given
        String username = "user";
        Integer bookId = 1;
        Integer quantity = 2;

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

        doReturn(Optional.of(userEntity)).when(userRepository).findById(anyString());
        doReturn(Optional.empty()).when(repository).findByUser(any(UserEntity.class));
        doAnswer(invocationOnMock -> {
            CartEntity arg = invocationOnMock.getArgument(0);
            arg.setId(1);
            return arg;
        }).when(repository).save(any(CartEntity.class));
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyInt());

        // when
        CartEntity actual = service.addItem(username, bookId, quantity);

        // then
        assertNotNull(actual);
        assertEquals(userEntity.getUsername(), actual.getUser().getUsername());
        assertEquals(1, actual.getItems().size());
        assertEquals(quantity, actual.getItems().get(0).getQuantity());
        assertEquals(bookEntity.getTitle(), actual.getItems().get(0).getBook().getTitle());
    }

    /**
     * Given created Cart with Item, a username, a book id to increase its quantity,
     * When adding item into the cart
     * Then, it should update the cart for the user and increase the book quantity
     */
    @Test
    void givenCreatedCart_whenAddingItemAlreadyThere_thenReturnUpdateCartIncreasingItemQuantity() {
        // given
        String username = "user";
        Integer bookId = 1;
        Integer quantity = 2;
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

        doReturn(Optional.of(userEntity)).when(userRepository).findById(anyString());
        doReturn(Optional.of(cartEntity)).when(repository).findByUser(any(UserEntity.class));
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyInt());
        doAnswer(invocationOnMock -> {
            CartEntity arg = invocationOnMock.getArgument(0);
            arg.setId(1);
            return arg;
        }).when(repository).save(any(CartEntity.class));


        // when
        CartEntity actual = service.addItem(username, bookId, quantity);

        // then
        assertNotNull(actual);
        assertEquals(userEntity.getUsername(), actual.getUser().getUsername());
        assertEquals(cartEntity.getItems().size(), actual.getItems().size());
        assertEquals(quantityAlreadyAdded + quantity, actual.getItems().get(0).getQuantity());
        assertEquals(bookEntity.getTitle(), actual.getItems().get(0).getBook().getTitle());
    }

    /**
     * Given a cart with an item and a book to remove from the cart,
     * When deleting a book from a cart
     * Then, it should remove the book from the cart
     */
    @Test
    void givenCartAndBookToRemove_whenDeletingBookFromCart_thenReturnCartWithoutBook() {
        // given
        String username = "user";
        Integer bookId = 1;
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

        doReturn(Optional.of(userEntity)).when(userRepository).findById(anyString());
        doReturn(Optional.of(cartEntity)).when(repository).findByUser(any(UserEntity.class));
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyInt());
        doAnswer(invocationOnMock -> {
            CartEntity arg = invocationOnMock.getArgument(0);
            arg.setId(1);
            return arg;
        }).when(repository).save(any(CartEntity.class));

        // when

        CartEntity actual = service.deleteItem(username, bookId);

        // then
        assertNotNull(actual);
        assertEquals(userEntity.getUsername(), actual.getUser().getUsername());
        assertEquals(0, actual.getItems().size());
    }

    /**
     * Given a cart with an item and a book to remove from the cart that is not in the cart,
     * When deleting a book from a cart
     * Then, it shouldn't do anything, just move on and return the cart as it was
     */
    @Test
    void givenCartAndInvaliBookToRemove_whenDeletingBookFromCart_thenReturnCart() {
        // given
        String username = "user";
        Integer bookIdNotInCart = 2;
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

        doReturn(Optional.of(userEntity)).when(userRepository).findById(anyString());
        doReturn(Optional.of(cartEntity)).when(repository).findByUser(any(UserEntity.class));
        doReturn(Optional.of(bookEntity)).when(bookRepository).findById(anyInt());
        doAnswer(invocationOnMock -> {
            CartEntity arg = invocationOnMock.getArgument(0);
            arg.setId(1);
            return arg;
        }).when(repository).save(any(CartEntity.class));

        // when

        CartEntity actual = service.deleteItem(username, bookIdNotInCart);

        // then
        assertNotNull(actual);
        assertEquals(userEntity.getUsername(), actual.getUser().getUsername());
        assertEquals(cartEntity.getItems().size(), actual.getItems().size());
        assertEquals(quantityAlreadyAdded, actual.getItems().get(0).getQuantity());
        assertEquals(bookEntity.getTitle(), actual.getItems().get(0).getBook().getTitle());
    }

    /**
     * Given an already created cart with an item
     * When getting the cart
     * Then, return the cart
     */
    @Test
    void givenCart_whenGettingCart_thenReturnCart() {
        // given
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

        doReturn(Optional.of(userEntity)).when(userRepository).findById(anyString());
        doReturn(Optional.of(cartEntity)).when(repository).findByUser(any(UserEntity.class));


        // when
        CartEntity actual = service.getCart(username);

        // then
        assertNotNull(actual);
        assertEquals(userEntity.getUsername(), actual.getUser().getUsername());
        assertEquals(cartEntity.getItems().size(), actual.getItems().size());
        assertEquals(quantityAlreadyAdded, actual.getItems().get(0).getQuantity());
        assertEquals(bookEntity.getTitle(), actual.getItems().get(0).getBook().getTitle());
    }


    /**
     * Given a not yet created cart for a user
     * When getting the cart
     * Then, return a new cart without items
     */
    @Test
    void givenNotYetCreatedCart_whenGettingCart_thenReturnNewCart() {
        // given
        String username = "user";

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password("password")
                .role("ROLE_USER")
                .build();


        doReturn(Optional.of(userEntity)).when(userRepository).findById(anyString());
        doReturn(Optional.empty()).when(repository).findByUser(any(UserEntity.class));
        doAnswer(invocationOnMock -> {
            CartEntity arg = invocationOnMock.getArgument(0);
            arg.setId(1);
            return arg;
        }).when(repository).save(any(CartEntity.class));

        // when
        CartEntity actual = service.getCart(username);

        // then
        assertNotNull(actual);
        assertEquals(userEntity.getUsername(), actual.getUser().getUsername());
        assertEquals(0, actual.getItems().size());
    }

}
