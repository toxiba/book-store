package com.example.bookstore.controllers;

import com.example.bookstore.dtos.CartCheckoutDto;
import com.example.bookstore.dtos.CartDto;
import com.example.bookstore.entities.CartEntity;
import com.example.bookstore.mappers.CartCheckoutMapper;
import com.example.bookstore.mappers.CartMapper;
import com.example.bookstore.services.ICartService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/cart")
@Slf4j
public class CartController {

    private ICartService service;

    private CartMapper mapper;

    private CartCheckoutMapper cartCheckoutMapper;

    @Autowired
    public CartController(ICartService service, CartMapper mapper, CartCheckoutMapper cartCheckoutMapper) {
        this.service = service;
        this.mapper = mapper;
        this.cartCheckoutMapper = cartCheckoutMapper;
    }

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public CartDto getCart(Principal principal) { // injecting the user authenticated
        log.info("getCart({})", principal.getName());
        return mapper.toDto(service.getCart(principal.getName()));
    }

    @PostMapping("/item/{bookId}/quantity/{quantity}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public CartDto addCartItem(@PathVariable(name = "bookId") @Min(0) Integer bookId,
                               @PathVariable(name = "quantity") @Min(1) Integer quantity,
                               Principal principal) { // injecting the user authenticated
        log.info("addCartItem({}, {}, {})", bookId, quantity, principal.getName());
        return mapper.toDto(service.addItem(principal.getName(), bookId, quantity));
    }

    @DeleteMapping("/item/{bookId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public CartDto deleteCartItem(@PathVariable(name = "bookId") @Min(0) Integer bookId,
                               Principal principal) { // injecting the user authenticated
        log.info("addCartItem({}, {})", bookId, principal.getName());
        return mapper.toDto(service.deleteItem(principal.getName(), bookId));
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public CartCheckoutDto checkout(Principal principal) { // injecting the user authenticated
        log.info("checkout({})", principal.getName());
        return cartCheckoutMapper.toDto(service.getCart(principal.getName()));
    }
}
