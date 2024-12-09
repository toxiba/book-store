package com.example.bookstore.services;

import com.example.bookstore.entities.BookEntity;

import java.util.List;

public interface IBookService {

    BookEntity getBook(Integer id);

    List<BookEntity> getBooks();

    void deleteBook(Integer id);

    BookEntity createBook(BookEntity entity);

    BookEntity updateBook(Integer id, BookEntity entity);

}
