package com.example.bookstore.controllers;

import com.example.bookstore.dtos.BookDto;
import com.example.bookstore.mappers.BookMapper;
import com.example.bookstore.services.IBookService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Slf4j
public class BookController {

    private IBookService service;

    private BookMapper mapper;

    @Autowired
    public BookController(IBookService service, BookMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> getBooks() {
        log.info("getBooks()");
        return mapper.toDtoList(service.getBooks());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getBook(@PathVariable("id") Integer id) {
        log.info("getBook({})", id);
        return mapper.toDto(service.getBook(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@Valid @RequestBody BookDto book) {
        log.info("createBook() - {}", book);
        return mapper.toDto(service.createBook(mapper.toEntity(book)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BookDto updateBook(@PathVariable("id") Integer id, @Valid @RequestBody BookDto book) {
        log.info("updateBook({}) - {}", id,  book);
        return mapper.toDto(service.updateBook(id, mapper.toEntity(book)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@PathVariable("id") Integer id) {
        log.info("deleteBook({})", id);
        service.deleteBook(id);
    }

}
