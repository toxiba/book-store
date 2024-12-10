package com.example.bookstore.respositories;

import com.example.bookstore.entities.BookEntity;
import com.example.bookstore.entities.CartEntity;
import com.example.bookstore.entities.CartItemEntity;
import com.example.bookstore.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("tests")
class CartRepositoryTest {

    @Autowired
    private CartRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

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
        assertNotNull(repository);
        assertNotNull(userRepository);
        assertNotNull(bookRepository);
    }

    /**
     * Given a Cart,
     * When saving
     * Then, it should return the saved cart
     */
    @Test
    void givenCart_whenSaving_thenReturnSavedCart() {
        // given
        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .username("username")
                .password("password")
                .role("ROLE_USER")
                .build());

        BookEntity bookEntity = bookRepository.save(BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build());

        BookEntity bookEntity2 = bookRepository.save(BookEntity.builder()
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(321))
                .build());

        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .book(bookEntity)
                .quantity(2)
                .build();

        CartItemEntity cartItemEntity2 = CartItemEntity.builder()
                .book(bookEntity2)
                .quantity(4)
                .build();

        List<CartItemEntity> cartItems = new ArrayList<>();
        cartItems.add(cartItemEntity);
        cartItems.add(cartItemEntity2);

        CartEntity entity = CartEntity.builder()
                .user(userEntity)
                .items(cartItems)
                .build();

        // when
        CartEntity actual = repository.save(entity);

        // then
        assertNotNull(actual.getId());
        assertEquals(entity.getUser().getUsername(), actual.getUser().getUsername());
        assertEquals(entity.getItems().size(), actual.getItems().size());
    }



    /**
     * Given a User and CartEntity not yet created
     * When calling a custom findByUser
     * Then, it should return an empty Optional
     */
    @Test
    void givenUserWithoutCart_whenCallingFindByUser_thenReturnSavedCart() {
        // given
        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .username("username")
                .password("password")
                .role("ROLE_USER")
                .build());

        // when
        Optional<CartEntity> opt = repository.findByUser(userEntity);

        // then
        assertNotNull(opt);
        assertTrue(opt.isEmpty());
    }

    /**
     * Given a User and CartEntity already created
     * When calling a custom findByUser
     * Then, it should return the CartEntity
     */
    @Test
    void givenUser_whenCallingFindByUser_thenReturnSavedCart() {
        // given
        UserEntity userEntity = userRepository.save(UserEntity.builder()
                .username("username")
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
                .quantity(2)
                .build();

        List<CartItemEntity> cartItems = new ArrayList<>();
        cartItems.add(cartItemEntity);

        CartEntity cart = repository.save(CartEntity.builder()
                .user(userEntity)
                .items(cartItems)
                .build());

        // when
        Optional<CartEntity> opt = repository.findByUser(userEntity);

        // then
        assertNotNull(opt);
        assertTrue(opt.isPresent());

        CartEntity actual = opt.get();
        assertEquals(cart.getUser().getUsername(), actual.getUser().getUsername());
        assertEquals(cart.getItems().size(), actual.getItems().size());
    }

}
