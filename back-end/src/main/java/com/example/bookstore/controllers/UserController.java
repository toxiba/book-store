package com.example.bookstore.controllers;

import com.example.bookstore.dtos.UserDto;
import com.example.bookstore.mappers.UserMapper;
import com.example.bookstore.services.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private IUserService service;

    private UserMapper mapper;

    @Autowired
    public UserController(IUserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody UserDto user) {
        log.info("register({})", user.getUsername());
        return mapper.toDto(service.createUser(mapper.toEntity(user)));
    }

}
