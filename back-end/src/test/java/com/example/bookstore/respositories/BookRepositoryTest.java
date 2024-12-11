package com.example.bookstore.respositories;

import com.example.bookstore.entities.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("tests")
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    /**
     * Given the test objects to support the tests,
     * When starting to test
     * Then, it should assert that all of them are not null
     */
    @Test
    void givenTestObjects_whenTesting_thenAssertNotNull() {
        assertNotNull(repository);
    }


    /**
     * Given a BookEntity,
     * When calling the save method
     * Then, it should save the entity and return the book saved
     */
    @Test
    void givenBook_whenSaving_thenReturnSavedBook() {
        // given
        BookEntity entity = BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build();

        // when
        BookEntity actual = repository.save(entity);

        // then
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(entity.getTitle(), actual.getTitle());
        assertEquals(entity.getAuthor(), actual.getAuthor());
        assertEquals(entity.getPrice(), actual.getPrice());
    }

    /**
     * Given a valid id,
     * When calling the findById method
     * Then, it should find and return an optional filled with the correct book.
     */
    @Test
    void givenValidId_whenCallingFindById_thenReturnBook() {
        // given
        BookEntity entity = BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build();

        BookEntity saved = repository.save(entity);

        Integer validId = saved.getId();

        // when
        Optional<BookEntity> actual = repository.findById(validId);

        // then
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(entity.getTitle(), actual.get().getTitle());
        assertEquals(entity.getAuthor(), actual.get().getAuthor());
        assertEquals(entity.getPrice(), actual.get().getPrice());
    }

    /**
     * Given an invalid id,
     * When calling the findById method
     * Then, it should return an empty optional
     */
    @Test
    void givenInvalidBook_whenCallingFindById_thenReturnEmptyOptional() {
        // given
        BookEntity saved = repository.save(BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build());

        Integer invalidId = saved.getId() + 1; // making it invalid

        // when
        Optional<BookEntity> actual = repository.findById(invalidId);

        // then
        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }

    /**
     * Given some books persisted,
     * When calling the findAll,
     * Then, it should return a list with all the books.
     */
    @Test
    void givenBooksPersisted_whenCallingFindAll_thenReturnBooks() {
        // given
        repository.save(BookEntity.builder()
                .title("title1")
                .author("author1")
                .price(BigDecimal.valueOf(123))
                .build());

        repository.save(BookEntity.builder()
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(321))
                .build());

        // when
        List<BookEntity> list = repository.findAll();

        // then
        assertNotNull(list);

        list.sort(Comparator.comparing(BookEntity::getTitle));

        assertEquals(2, list.size());
        assertEquals("title1", list.get(0).getTitle());
        assertEquals("title2", list.get(1).getTitle());
    }

    /**
     * Given a BookEntity to be updated,
     * When calling save method
     * Then, it should save and return the updated book
     */
    @Test
    void givenBookToUpdate_whenSaving_thenReturnUpdatedBook() {
        // given
        BookEntity entity = BookEntity.builder()
                .title("title1")
                .author("author1")
                .price(BigDecimal.valueOf(123))
                .build();

        entity = repository.save(entity);

        BookEntity toUpdate = BookEntity.builder()
                .id(entity.getId())
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(321))
                .build();

        // when
        BookEntity actual = repository.save(toUpdate);

        // then
        assertNotNull(actual);
        assertEquals(toUpdate.getTitle(), actual.getTitle());
        assertEquals(toUpdate.getAuthor(), actual.getAuthor());
        assertEquals(toUpdate.getPrice(), actual.getPrice());
    }

    /**
     * Given a valid id,
     * When removing
     * Then, it should remove the book.
     */
    @Test
    void givenValidId_whenRemoving_thenRemoveBook() {
        // given
        BookEntity entity = BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build();

        BookEntity saved = repository.save(entity);

        Integer validId = saved.getId();

        // when
        repository.deleteById(validId);

        // then
        Optional<BookEntity> actual = repository.findById(validId);

        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }
}
