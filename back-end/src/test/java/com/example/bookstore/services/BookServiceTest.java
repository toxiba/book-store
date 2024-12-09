package com.example.bookstore.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {


    /**
     * Given a BookEntity,
     * When calling the create method
     * Then, it should save the entity and return the created entity
     */
    @Test
    @Disabled
    void givenBook_whenCreating_thenReturnSavedBook() {
        // given

        // when

        // then
    }

    /**
     * Given a valid id,
     * When calling the getBook method
     * Then, it should find and return an optional filled with the correct book.
     */
    @Test
    @Disabled
    void givenValidId_whenCallingFindById_thenReturnBook() {
        // given

        // when

        // then
    }

    /**
     * Given an invalid id,
     * When calling the getBook method
     * Then, it should return null
     */
    @Test
    @Disabled
    void givenInvalidId_whenCallingFindById_thenReturnNull() {
        // given

        // when

        // then
    }

    /**
     * Given some books persisted,
     * When calling the getBooks,
     * Then, it should return a list with all the books.
     */
    @Test
    @Disabled
    void givenBooksPersisted_whenCallingGetBooks_thenReturnBooks() {
        // given

        // when

        // then
    }

    /**
     * Given a Book to be updated,
     * When calling updateBook method
     * Then, it should save and return the updated book
     */
    @Test
    @Disabled
    void givenBookToUpdate_whenUpdatingBook_thenReturnUpdatedBook() {
        // given

        // when

        // then
    }

    /**
     * Given a Book to be updated and an invalid id,
     * When calling updateBook method
     * Then, it should throw not found exception
     */
    @Test
    @Disabled
    void givenInvalidIdAndBookToUpdate_whenUpdatingBook_thenThrowNotFoundException() {
        // given

        // when

        // then
    }

    /**
     * Given a valid id,
     * When removing
     * Then, it should call the deleteById 1 time.
     */
    @Test
    @Disabled
    void givenValidId_whenRemoving_thenRemoveBook() {
        // given

        // when

        // then
    }

    /**
     * Given an invalid id,
     * When removing
     * Then, it should throw not found exception
     */
    @Test
    @Disabled
    void givenInvalidId_whenRemoving_thenThrowNotFoundException() {
        // given

        // when

        // then
    }
}