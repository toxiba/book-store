package com.example.bookstore.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItemDto {

    private Integer id;

    private BookDto book;

    private Integer quantity;

}
