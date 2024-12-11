package com.example.bookstore.mappers;

import com.example.bookstore.dtos.CartItemDto;
import com.example.bookstore.entities.CartItemEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface CartItemMapper {

    CartItemDto toDto(CartItemEntity entity);

    List<CartItemDto> toDtoList(List<CartItemEntity> entities);

}
