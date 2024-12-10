package com.example.bookstore.services.impls;

import com.example.bookstore.entities.BookEntity;
import com.example.bookstore.entities.CartEntity;
import com.example.bookstore.entities.CartItemEntity;
import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.exceptions.NotFoundException;
import com.example.bookstore.respositories.BookRepository;
import com.example.bookstore.respositories.CartRepository;
import com.example.bookstore.respositories.UserRepository;
import com.example.bookstore.services.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements ICartService {

    private CartRepository repository;

    private UserRepository userRepository;

    private BookRepository bookRepository;

    @Autowired
    public CartServiceImpl(CartRepository repository, UserRepository userRepository, BookRepository bookRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public CartEntity addItem(String username, Integer bookId, Integer quantity) {
        log.info("addItem({}, {}, {})", username, bookId, quantity);

        // check if user exists
        UserEntity userEntity = getUser(username);

        // check if a cart is already created for that user
        CartEntity cartEntity = getCart(userEntity);

        // check if the book exists
        BookEntity bookEntity = getBook(bookId);

        // search if book is already there an add
        Optional<CartItemEntity> cartItemOpt = cartEntity.findCartItemByBookId(bookId);
        CartItemEntity cartItemEntity = null;
        if (cartItemOpt.isEmpty()) {
            // if cart item not yet created. create it with the book and the quantity
            log.debug("addItem({}, {}, {}) - book not yet present in the cart", username, bookId, quantity);
            cartItemEntity = CartItemEntity.builder()
                    .book(bookEntity)
                    .quantity(quantity)
                    .build();
            cartEntity.addItem(cartItemEntity);
        } else {
            // if cart item already created. update it adding quantity on top of the quantity
            cartItemEntity = cartItemOpt.get();
            cartItemEntity.setQuantity(cartItemEntity.getQuantity() + quantity);
        }

        return repository.save(cartEntity);
    }

    @Override
    public CartEntity deleteItem(String username, Integer bookId) {
        log.info("deleteItem({}, {})", username, bookId);

        // check if user exists
        UserEntity userEntity = getUser(username);

        // check if a cart is already created for that user
        CartEntity cartEntity = getCart(userEntity);

        // check if the book exists
        getBook(bookId);

        // search if book is already there an add
        Optional<CartItemEntity> cartItemOpt = cartEntity.findCartItemByBookId(bookId);
        CartItemEntity cartItemEntity = null;
        if (cartItemOpt.isEmpty()) {
            // if cart item not yet created. we just log a warn and move on
            log.warn("deleteItem({}, {}) - book not yet present in the cart", username, bookId);
        } else {
            // if cart item already created. update it adding quantity on top of the quantity
            cartItemEntity = cartItemOpt.get();
            cartEntity.removeItem(cartItemEntity);
        }

        return repository.save(cartEntity);
    }

    @Override
    public CartEntity getCart(String username) {
        log.info("getCart({})", username);

        // check if user exists
        UserEntity userEntity = getUser(username);

        // check if a cart is already created for that user
        return getCart(userEntity);
    }

    private UserEntity getUser(String username) {
        Optional<UserEntity> userOpt = userRepository.findById(username);
        if (userOpt.isEmpty()) {
            log.debug("getUser({}) - user not found", username);
            throw new NotFoundException("User not found with username: " + username);
        }

        return userOpt.get();
    }

    public CartEntity getCart(UserEntity userEntity) {
        Optional<CartEntity> opt = repository.findByUser(userEntity);

        if (opt.isEmpty()) {
            log.debug("getCart({}) - cart not yet created for the user.", userEntity.getUsername());

            return repository.save(repository.save(CartEntity.builder()
                    .user(userEntity)
                    .build()));
        } else {
            return opt.get();
        }
    }

    public BookEntity getBook(Integer bookId) {
        Optional<BookEntity> bookOpt = bookRepository.findById(bookId);

        if (bookOpt.isEmpty()) {
            log.debug("getBook({}) - book not found", bookId);
            throw new NotFoundException("Book not found with id: " + bookId);
        } else {
            return bookOpt.get();
        }
    }
}
