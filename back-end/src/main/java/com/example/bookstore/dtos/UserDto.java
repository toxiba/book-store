package com.example.bookstore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}