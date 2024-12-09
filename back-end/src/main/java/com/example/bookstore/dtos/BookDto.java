package com.example.bookstore.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookDto {

    private Integer id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotNull
    private BigDecimal price;

}