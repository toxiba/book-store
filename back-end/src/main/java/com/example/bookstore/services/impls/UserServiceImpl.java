package com.example.bookstore.services.impls;

import com.example.bookstore.entities.BookEntity;
import com.example.bookstore.entities.UserEntity;
import com.example.bookstore.exceptions.AlreadyExistsException;
import com.example.bookstore.exceptions.NotFoundException;
import com.example.bookstore.respositories.BookRepository;
import com.example.bookstore.respositories.UserRepository;
import com.example.bookstore.services.IBookService;
import com.example.bookstore.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(UserEntity entity) {
        log.info("createUser() - {}", entity);

        Optional<UserEntity> opt = repository.findById(entity.getUsername());

        if (opt.isPresent()) {
            log.debug("createUser() - username already exists {} ", entity.getUsername());

            throw new AlreadyExistsException("User already exists with username: " + entity.getUsername());
        } else {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            entity = repository.save(entity);
            log.debug("createBook - created = {}", entity.getUsername());

            return entity;
        }
    }
}
