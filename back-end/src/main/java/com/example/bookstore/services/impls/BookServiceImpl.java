package com.example.bookstore.services.impls;

import com.example.bookstore.entities.BookEntity;
import com.example.bookstore.exceptions.NotFoundException;
import com.example.bookstore.respositories.BookRepository;
import com.example.bookstore.services.IBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookServiceImpl implements IBookService {

    private BookRepository repository;

    @Autowired
    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookEntity getBook(Integer id) {
        log.info("getBook({})", id);
        Optional<BookEntity> opt = repository.findById(id);

        if (opt.isPresent()) {
            log.debug("getBook({}) - found = {}", id, opt.get());
            return opt.get();
        } else {
            log.debug("getBook({}) - not found", id);
            throw new NotFoundException("Book not found with id: " + id);
        }
    }

    @Override
    public List<BookEntity> getBooks() {
        log.info("getBooks()");

        List<BookEntity> list = repository.findAll();

        log.debug("getBooks() - {} books", list.size());
        return list;
    }

    @Override
    public void deleteBook(Integer id) {
        log.info("deleteBook({})", id);
        Optional<BookEntity> opt = repository.findById(id);

        if (opt.isPresent()) {
            log.debug("deleteBook({}) - found = {}", id, opt.get());
            repository.deleteById(id);
            log.info("deleteBook({}) - deleted", id);
        } else {
            log.debug("deleteBook({}) - not found", id);
            throw new NotFoundException("Book not found with id: " + id);
        }
    }

    @Override
    public BookEntity createBook(BookEntity entity) {
        log.info("createBook() - {}", entity);

        entity.setId(null); // making sure we are creating it

        entity = repository.save(entity);
        log.debug("createBook - created = {}", entity);

        return entity;
    }

    @Override
    public BookEntity updateBook(Integer id, BookEntity entity) {
        log.info("updateBook({}): {}", id, entity);

        Optional<BookEntity> opt = repository.findById(id);

        if (opt.isPresent()) {

            log.debug("updateBook({}) - found = {}", id, opt.get());
            entity.setId(id); // making sure we are updating it

            entity = repository.save(entity);
            log.debug("updateBook({}) - updated = {}", id, entity);
            return entity;
        } else {
            log.debug("updateBook({}) - found", id);
            throw new NotFoundException("Book not found with id: " + id);
        }
    }
}
