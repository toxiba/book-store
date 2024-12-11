package com.example.bookstore.services;

import com.example.bookstore.entities.BookEntity;
import com.example.bookstore.exceptions.NotFoundException;
import com.example.bookstore.respositories.BookRepository;
import com.example.bookstore.services.impls.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("tests")
class BookServiceTest {

    @InjectMocks
    private BookServiceImpl service;

    @Mock
    private BookRepository repository;

    /**
     * Given the test objects to support the tests,
     * When starting to test
     * Then, it should assert that all of them are not null
     */
    @Test
    void givenTestObjects_whenTesting_thenAssertNotNull() {
        assertNotNull(service);
        assertNotNull(repository);
    }

    /**
     * Given a BookEntity,
     * When calling the create method
     * Then, it should save the entity and return the created entity
     */
    @Test
    void givenBook_whenCreating_thenReturnSavedBook() {
        // given
        BookEntity entity = BookEntity.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build();

        doAnswer(invocation -> {
            BookEntity e = invocation.getArgument(0);
            e.setId(1);
            return e;
        }).when(repository).save(any(BookEntity.class));

        // when
        BookEntity actual = service.createBook(entity);

        // then
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(entity.getTitle(), actual.getTitle());
        assertEquals(entity.getAuthor(), actual.getAuthor());
        assertEquals(entity.getPrice(), actual.getPrice());
    }

    /**
     * Given a valid id,
     * When calling the getBook method
     * Then, it should find and return an optional filled with the correct book.
     */
    @Test
    void givenValidId_whenCallingFindById_thenReturnBook() {
        // given
        BookEntity entity = BookEntity.builder()
                .id(1)
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(123))
                .build();

        Integer validId = entity.getId();

        doReturn(Optional.of(entity)).when(repository).findById(anyInt());

        // when
        BookEntity actual = service.getBook(validId);

        // then
        assertNotNull(actual);
        assertEquals(validId, actual.getId());
        assertEquals(entity.getTitle(), actual.getTitle());
        assertEquals(entity.getAuthor(), actual.getAuthor());
        assertEquals(entity.getPrice(), actual.getPrice());
    }

    /**
     * Given an invalid id,
     * When calling the getBook method
     * Then, it should return null
     */
    @Test
    void givenInvalidId_whenCallingFindById_thenReturnNull() {
        // given
        Integer invalidId = 1;

        doReturn(Optional.empty()).when(repository).findById(anyInt());

        // when/then
        assertThrows(NotFoundException.class, () -> service.getBook(invalidId));
    }

    /**
     * Given some books persisted,
     * When calling the getBooks,
     * Then, it should return a list with all the books.
     */
    @Test
    void givenBooksPersisted_whenCallingGetBooks_thenReturnBooks() {
        // given
        List<BookEntity> list = new ArrayList<>();

        list.add(BookEntity.builder()
                .title("title1")
                .author("author1")
                .price(BigDecimal.valueOf(123))
                .build());

        list.add(BookEntity.builder()
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(321))
                .build());

        doReturn(list).when(repository).findAll();

        // when
        List<BookEntity> listActual = service.getBooks();

        // then
        assertNotNull(list);

        list.sort(Comparator.comparing(BookEntity::getTitle));

        assertEquals(2, listActual.size());
        assertEquals("title1", listActual.get(0).getTitle());
        assertEquals("title2", listActual.get(1).getTitle());
    }

    /**
     * Given a Book to be updated,
     * When calling updateBook method
     * Then, it should save and return the updated book
     */
    @Test
    void givenBookToUpdate_whenUpdatingBook_thenReturnUpdatedBook() {
        // given
        BookEntity saved = BookEntity.builder()
                .id(1)
                .title("title1")
                .author("author1")
                .price(BigDecimal.valueOf(123))
                .build();

        BookEntity toUpdate = BookEntity.builder()
                .id(1)
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(321))
                .build();

        Integer id = toUpdate.getId();

        doReturn(Optional.of(saved)).when(repository).findById(anyInt());
        doReturn(toUpdate).when(repository).save(any(BookEntity.class));

        // when
        BookEntity actual = service.updateBook(id, toUpdate);

        // then
        assertNotNull(actual);
        assertEquals(toUpdate.getTitle(), actual.getTitle());
        assertEquals(toUpdate.getAuthor(), actual.getAuthor());
        assertEquals(toUpdate.getPrice(), actual.getPrice());
    }

    /**
     * Given a Book to be updated and an invalid id,
     * When calling updateBook method
     * Then, it should throw not found exception
     */
    @Test
    void givenInvalidIdAndBookToUpdate_whenUpdatingBook_thenThrowNotFoundException() {
        // given
        Integer invalidId = 1;

        BookEntity toUpdate = BookEntity.builder()
                .id(invalidId)
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(321))
                .build();

        doReturn(Optional.empty()).when(repository).findById(anyInt());

        // when/then
        assertThrows(NotFoundException.class, () -> service.updateBook(invalidId, toUpdate));
    }

    /**
     * Given a valid id,
     * When calling delete method
     * Then, it should call the deleteById 1 time.
     */
    @Test
    void givenValidId_whenDeleting_thenDeleteBook() {
        // given
        Integer validId = 1;
        BookEntity saved = BookEntity.builder()
                .id(validId)
                .title("title1")
                .author("author1")
                .price(BigDecimal.valueOf(321))
                .build();

        doReturn(Optional.of(saved)).when(repository).findById(anyInt());

        // when
        service.deleteBook(validId);

        // then
        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(1)).deleteById(anyInt());
    }

    /**
     * Given an invalid id,
     * When calling delete method
     * Then, it should throw not found exception
     */
    @Test
    void givenInvalidId_whenDeleting_thenThrowNotFoundException() {
        // given
        Integer invalidId = 1;

        doReturn(Optional.empty()).when(repository).findById(anyInt());

        // when/then
        assertThrows(NotFoundException.class, () -> service.deleteBook(invalidId));

        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(0)).deleteById(anyInt());
    }
}