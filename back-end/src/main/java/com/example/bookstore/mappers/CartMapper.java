package com.example.bookstore.mappers;

import com.example.bookstore.dtos.CartDto;
import com.example.bookstore.entities.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {

    @Mappings({
        @Mapping(target = "username", source = "user.username")
    })
    CartDto toDto(CartEntity entity);
}
