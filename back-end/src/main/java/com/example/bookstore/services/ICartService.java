package com.example.bookstore.services;

import com.example.bookstore.entities.CartEntity;

public interface ICartService {

    CartEntity addItem(String username, Integer bookId, Integer quantity);

    CartEntity deleteItem(String username, Integer bookId);

    CartEntity getCart(String username);
}
