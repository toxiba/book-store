package com.example.bookstore.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("tests")
class BookRepositoryTest {


    /**
     * Given a BookEntity,
     * When calling the save method
     * Then, it should save the entity and return the book saved
     */
    @Test
    @Disabled
    void givenBook_whenSaving_thenReturnSavedBook() {
        // given

        // when

        // then
    }

    /**
     * Given a valid id,
     * When calling the findById method
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
     * When calling the findById method
     * Then, it should return an empty optional
     */
    @Test
    @Disabled
    void givenInvalidBook_whenCallingFindById_thenReturnEmptyOptional() {
        // given

        // when

        // then
    }

    /**
     * Given some books persisted,
     * When calling the findAll,
     * Then, it should return a list with all the books.
     */
    @Test
    @Disabled
    void givenBooksPersisted_whenCallingFindAll_thenReturnBooks() {
        // given

        // when

        // then
    }

    /**
     * Given a BookEntity to be updated,
     * When calling save method
     * Then, it should save and return the updated book
     */
    @Test
    @Disabled
    void givenBookToUpdate_whenSaving_thenReturnUpdatedBook() {
        // given

        // when

        // then
    }

    /**
     * Given a valid id,
     * When removing
     * Then, it should remove the book.
     */
    @Test
    @Disabled
    void givenValidId_whenRemoving_thenRemoveBook() {
        // given

        // when

        // then
    }
}
