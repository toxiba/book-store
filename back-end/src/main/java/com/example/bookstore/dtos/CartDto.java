package com.example.bookstore.dtos;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartDto {

    private Integer id;

    private String username;

    private List<CartItemDto> items;

}
