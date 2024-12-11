package com.example.bookstore.dtos;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItemCheckoutDto {

    private BookDto book;

    private Integer quantity;

    private BigDecimal subtotal;

}
