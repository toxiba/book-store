package com.example.bookstore.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartCheckoutDto {

    private String username;

    private List<CartItemCheckoutDto> items;

    private BigDecimal total;

}
