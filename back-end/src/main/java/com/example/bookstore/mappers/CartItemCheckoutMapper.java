package com.example.bookstore.mappers;

import com.example.bookstore.dtos.CartItemCheckoutDto;
import com.example.bookstore.entities.CartItemEntity;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface CartItemCheckoutMapper {

    @Mappings({
        @Mapping(target = "subtotal", ignore = true)
    })
    CartItemCheckoutDto toDto(CartItemEntity entity);

    List<CartItemCheckoutDto> toDtoList(List<CartItemEntity> entities);

    @AfterMapping
    default void calculateSubTotal(@MappingTarget CartItemCheckoutDto cartItemCheckoutDto, CartItemEntity entity) {
        Integer quantity = entity.getQuantity();
        BigDecimal price = entity.getBook().getPrice();

        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

        cartItemCheckoutDto.setSubtotal(subtotal);
    }

}
