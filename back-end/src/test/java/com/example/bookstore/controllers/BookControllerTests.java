package com.example.bookstore.controllers;

import com.example.bookstore.configs.TestConfig;
import com.example.bookstore.dtos.BookDto;
import com.example.bookstore.entities.BookEntity;
import com.example.bookstore.exceptions.NotFoundException;
import com.example.bookstore.mappers.BookMapper;
import com.example.bookstore.services.IBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
@Import({TestConfig.class})
class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IBookService service;

    @Autowired
    private BookMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BOOKS_CONTROLLER_URI = "/api/v1/books";

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
        assertNotNull(objectMapper);
    }

    /**
     * Given a Book,
     * When calling the create endpoint
     * Then, it should return the book created
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenBook_whenCreating_thenReturnSavedBook() throws Exception {
        // given
        Double price = 123.0;
        Integer id = 1;
        BookDto dto = BookDto.builder()
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(price))
                .build();

        doAnswer(invocation -> {
            BookEntity e = invocation.getArgument(0);
            e.setId(id);
            return e;
        }).when(service).createBook(ArgumentMatchers.any(BookEntity.class));

        String jsonContent = objectMapper.writeValueAsString(dto);

        // when
        ResultActions result = mockMvc.perform(post(BOOKS_CONTROLLER_URI)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", allOf(notNullValue(), is(id))))
                .andExpect(jsonPath("$.title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$.author", equalTo(dto.getAuthor())))
                .andExpect(jsonPath("$.price", is(price)))
        ;
    }

    /**
     * Given an invalid Book,
     * When calling the create endpoint
     * Then, it should return bad request with a list of missing fields
     */
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void givenInvalidBook_whenCreating_thenThrowBadRequest() throws Exception {
        // given
        Double price = 123.0;
        BookDto dto = BookDto.builder()
                // missing required fields
                .price(BigDecimal.valueOf(price))
                .build();

        String jsonContent = objectMapper.writeValueAsString(dto);

        // when
        ResultActions result = mockMvc.perform(post(BOOKS_CONTROLLER_URI)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.author", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.title", notNullValue()))
        ;
    }

    /**
     * Given a Book to update,
     * When calling the update endpoint
     * Then, it should return the book updated
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenBook_whenUpdating_thenReturnSavedBook() throws Exception {
        // given
        Double price = 123.0;
        Integer id = 1;
        BookDto dto = BookDto.builder()
                .id(id)
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(price))
                .build();

        doAnswer(invocation -> invocation.getArgument(1))
                .when(service)
                .updateBook(anyInt(), ArgumentMatchers.any(BookEntity.class));

        String jsonContent = objectMapper.writeValueAsString(dto);

        // when
        ResultActions result = mockMvc.perform(put(BOOKS_CONTROLLER_URI + "/" + id)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", allOf(notNullValue(), is(id))))
                .andExpect(jsonPath("$.title", equalTo(dto.getTitle())))
                .andExpect(jsonPath("$.author", equalTo(dto.getAuthor())))
                .andExpect(jsonPath("$.price", is(price)))
        ;
    }

    /**
     * Given an invalid Book,
     * When calling the update method
     * Then, it should return bad request with a list of missing fields
     */
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void givenInvalidBook_whenUpdating_thenThrowBadRequest() throws Exception {
        // given
        Double price = 123.0;
        Integer id = 1;
        BookDto dto = BookDto.builder()
                .id(id)
                // missing required fields
                .price(BigDecimal.valueOf(price))
                .build();

        String jsonContent = objectMapper.writeValueAsString(dto);

        // when
        ResultActions result = mockMvc.perform(put(BOOKS_CONTROLLER_URI + "/" + id)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.author", notNullValue()))
                .andExpect(jsonPath("$.validationErrors.title", notNullValue()))
        ;
    }

    /**
     * Given an invalid id,
     * When calling the delete method
     * Then, it should return bad request
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenUpdating_thenThrowNotFound() throws Exception {
        // given
        Double price = 123.0;
        Integer id = 1;
        BookDto dto = BookDto.builder()
                .id(id)
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(price))
                .build();

        String jsonContent = objectMapper.writeValueAsString(dto);

        doThrow(NotFoundException.class)
                .when(service)
                .updateBook(anyInt(), ArgumentMatchers.any(BookEntity.class));

        // when
        ResultActions result = mockMvc.perform(put(BOOKS_CONTROLLER_URI + "/" + id)
                .content(jsonContent)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isNotFound())
        ;
    }

    /**
     * Given an id,
     * When calling the delete method
     * Then, it should remove the book and return ok
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenId_whenDeleting_thenReturnOK() throws Exception {
        // given
        Integer id = 1;

        doNothing().when(service).deleteBook(anyInt());

        // when
        ResultActions result = mockMvc.perform(delete(BOOKS_CONTROLLER_URI + "/" + id));

        // then
        result.andExpect(status().isOk())
        ;
    }

    /**
     * Given an invalid id,
     * When calling the delete method
     * Then, it should return not found
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenInvalidId_whenDeleting_thenReturnNotFound() throws Exception {
        // given
        Integer id = 1;

        doThrow(NotFoundException.class).when(service).deleteBook(anyInt());

        // when
        ResultActions result = mockMvc.perform(delete(BOOKS_CONTROLLER_URI + "/" + id));

        // then
        result.andExpect(status().isNotFound())
        ;
    }

    /**
     * Given a valid id,
     * When calling the get by id
     * Then, it should return the book
     */
    @Test
    @WithAnonymousUser
    void givenValidId_whenGettingById_thenReturnBook() throws Exception {
        // given
        Double price = 123.0;
        Integer id = 1;
        BookEntity entity = BookEntity.builder()
                .id(id)
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(price))
                .build();

        doReturn(entity).when(service).getBook(anyInt());

        // when
        ResultActions result = mockMvc.perform(get(BOOKS_CONTROLLER_URI + "/" + id));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", allOf(notNullValue(), is(id))))
                .andExpect(jsonPath("$.title", equalTo(entity.getTitle())))
                .andExpect(jsonPath("$.author", equalTo(entity.getAuthor())))
                .andExpect(jsonPath("$.price", is(price)))
        ;
    }

    /**
     * Given an invalid id,
     * When calling the get by id
     * Then, it should return not found
     */
    @Test
    @WithAnonymousUser
    void givenInvalidId_whenGettingById_thenReturnNotFound() throws Exception {
        // given
        Integer id = 1;

        doThrow(NotFoundException.class).when(service).getBook(anyInt());

        // when
        ResultActions result = mockMvc.perform(get(BOOKS_CONTROLLER_URI + "/" + id));

        // then
        result.andExpect(status().isNotFound())
        ;
    }

    /**
     * Given a list of books persited,
     * When calling the get all
     * Then, it should return a lsit of books
     */
    @Test
    @WithAnonymousUser
    void givenListsPersisted_whenGettingAll_thenReturnListOfBooks() throws Exception {
        // given
        Double price = 123.0;
        BookEntity entity = BookEntity.builder()
                .id(1)
                .title("title")
                .author("author")
                .price(BigDecimal.valueOf(price))
                .build();
        BookEntity entity2 = BookEntity.builder()
                .id(2)
                .title("title2")
                .author("author2")
                .price(BigDecimal.valueOf(price + 1))
                .build();


        doReturn(List.of(entity, entity2)).when(service).getBooks();

        // when
        ResultActions result = mockMvc.perform(get(BOOKS_CONTROLLER_URI));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", allOf(notNullValue(), is(entity.getId()))))
                .andExpect(jsonPath("$[0].title", equalTo(entity.getTitle())))
                .andExpect(jsonPath("$[0].author", equalTo(entity.getAuthor())))
                .andExpect(jsonPath("$[0].price", is(price)))
                .andExpect(jsonPath("$[1].id", allOf(notNullValue(), is(entity2.getId()))))
                .andExpect(jsonPath("$[1].title", equalTo(entity2.getTitle())))
                .andExpect(jsonPath("$[1].author", equalTo(entity2.getAuthor())))
                .andExpect(jsonPath("$[1].price", is(price + 1)))
        ;
    }

}
